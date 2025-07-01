package com.example.BankApp.Service.impl;

import com.example.BankApp.Config.JwtTokenProvider;
import com.example.BankApp.Entity.Role;
import com.example.BankApp.Entity.User;
import com.example.BankApp.Repository.TransactionRepo;
import com.example.BankApp.Repository.UserRepository;
import com.example.BankApp.Utils.AccountUtils;
import com.example.BankApp.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service

public class UserServiceimpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * creating an account - saving a new user into db
         * check if user already has an account
         */

        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountinfo(null)
                    .build();
            
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativeNumber(userRequest.getAlternativeNumber())
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_ADMIN"))
                .build();

        User saveduser = userRepository.save(newUser);

        // Send email Alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveduser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your  Account has been Successfully Created.\n" +
                        "Your Account Details: \n"+ "Account Name: "+ saveduser.getFirstName()+" "+saveduser.getLastName()+" "+saveduser.getOtherName()+
                        "\nAccount Number: "+saveduser.getAccountNumber()+"\nNo Reply to this mail")
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATON_MESSAGE)
                .accountinfo(Accountinfo.builder()
                        .accountBalance(saveduser.getAccountBalance())
                        .accountNumber(saveduser.getAccountNumber())
                        .accountName(saveduser.getFirstName()+" "+ saveduser.getLastName()+" "+saveduser.getOtherName())
                        .build())
                .build();
    }

    public BankResponse login(LoginDto loginDto){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );
        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You're logged in")
                .recipient(loginDto.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request, please contact your bank")
                .build();

        emailService.sendEmailAlert(loginAlert);
        return BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();
    }


    // balance Enquiry , name Enquiry , credit , debit , transfer
    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // check if the provided account number exists in db
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountinfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountinfo(Accountinfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+ foundUser.getLastName()+" "+ foundUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName()+" "+ foundUser.getLastName()+" "+ foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        // check if account is exist
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountinfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
           // Saving the updated Data
        userRepository.save(userToCredit);

        // save  transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactiontype("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountinfo(Accountinfo.builder()
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        /*
          check if the account exists
          check if the amount you intend  to withdraw is not more than  the current account balance
         */
        Boolean isAccountexist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountexist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountinfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
         BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
         BigInteger debitAmount = request.getAmount().toBigInteger();
         if (availableBalance.intValue() < debitAmount.intValue()){
             return BankResponse.builder()
                     .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                     .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                     .accountinfo(null)
                     .build();
         }
         else {
             userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
             userRepository.save(userToDebit);

             TransactionDto transactionDto = TransactionDto.builder()
                     .accountNumber(userToDebit.getAccountNumber())
                     .transactiontype("DEBIT")
                     .amount(request.getAmount())
                     .build();

             transactionService.saveTransaction(transactionDto);

             return BankResponse.builder()
                     .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                     .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                     .accountinfo(Accountinfo.builder()
                             .accountNumber(request.getAccountNumber())
                             .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName())
                             .accountBalance(userToDebit.getAccountBalance())
                             .build())
                     .build();
         }
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        /*
             Check if the Account os exists or not
             get the account to debit
             check if the amount i'm debiting is not more than the current balance
             debit the account
             get the account to credit
             credit to account
         */
        Boolean isSourceAccountexist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        Boolean isDestinationAccountexist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!isSourceAccountexist && !isDestinationAccountexist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage("Source and Destination accounts do not exist.")
                    .accountinfo(null)
                    .build();
        }
        if (!isSourceAccountexist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage("Source account does not exist.")
                    .accountinfo(null)
                    .build();
        }

        if (!isDestinationAccountexist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage("Destination account does not exist.")
                    .accountinfo(null)
                    .build();
        }
        User sourceAccountuser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if (request.getAmount().compareTo(sourceAccountuser.getAccountBalance())>0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountinfo(null)
                    .build();
        }
        sourceAccountuser.setAccountBalance(sourceAccountuser.getAccountBalance().subtract(request.getAmount()));
        String sourceUserName = sourceAccountuser.getFirstName()+" "+sourceAccountuser.getLastName()+" "+sourceAccountuser.getOtherName();
        userRepository.save(sourceAccountuser);

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountuser.getEmail())
                .messageBody("The sum of "+request.getAmount()+"has been deducted from your account ! Your current balance is "+sourceAccountuser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);


         User destinationAccount = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
         destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(request.getAmount()));
        // String destinationUserName =destinationAccount.getFirstName()+" "+destinationAccount.getLastName()+" "+destinationAccount.getOtherName();

        userRepository.save(destinationAccount);
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccount.getEmail())
                .messageBody("The sum of "+request.getAmount()+" has been sent to your account from"+ sourceUserName+"  ! Your current balance is "+destinationAccount.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinationAccount.getAccountNumber())
                .transactiontype("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountinfo(null)
                .build();
    }



}
