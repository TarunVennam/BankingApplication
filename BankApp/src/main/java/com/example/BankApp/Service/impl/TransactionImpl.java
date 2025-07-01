package com.example.BankApp.Service.impl;

import com.example.BankApp.Entity.Transaction;
import com.example.BankApp.Repository.TransactionRepo;
import com.example.BankApp.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class TransactionImpl implements TransactionService{

    @Autowired
    TransactionRepo transactionRepo;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactiontype(transactionDto.getTransactiontype())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();
        transactionRepo.save(transaction);
        System.out.println("Transaction saved successfully");
    }
}
