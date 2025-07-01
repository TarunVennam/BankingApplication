package com.example.BankApp.Utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = " This user already has an account created!";

    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATON_MESSAGE = "Account has been successfully created!";

    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with the provided Account Number does not exist";

    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found";

    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User get Amount Succesfully";

    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance in your account";

    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account has been successfully debited";

    public static final String TRANSFER_SUCCESSFUL_CODE ="008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer successfull";



    public static String generateAccountNumber(){
        /**
         * 2025 + randomsixdigits
         */
        Year currentYear = Year.now();

        int min = 100000;

        int max = 999999;

        // generate random number between min and max

        int randNumber = (int) Math.floor(Math.random() * (max-min+1) +min);
        //Convert the current and randomNumber to Strings , then Concatenate them

        String year = String.valueOf(currentYear);

        String randomNumber = String.valueOf(randNumber);
        StringBuilder accounNo = new StringBuilder();
        return accounNo.append(year).append(randomNumber).toString();
    }

}
