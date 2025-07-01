package com.example.BankApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    private String sourceAccountNumber; // For Debit

    private String destinationAccountNumber; // For Credit

    private BigDecimal amount;
}
