package com.example.BankApp.Service.impl;

import com.example.BankApp.Entity.Transaction;
import com.example.BankApp.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);

}
