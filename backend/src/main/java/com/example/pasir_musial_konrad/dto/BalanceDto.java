package com.example.pasir_musial_konrad.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class BalanceDto{
    private double totalIncome;

    private double totalExpense;

    private double balance;

    @Builder
    public BalanceDto(double totalIncome, double totalExpense, double balance) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
    }
}
