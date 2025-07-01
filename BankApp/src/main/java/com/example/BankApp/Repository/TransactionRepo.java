package com.example.BankApp.Repository;

import com.example.BankApp.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountNumberAndCreatedAtBetween(
            String accountNumber, LocalDate startDate, LocalDate endDate);
}
