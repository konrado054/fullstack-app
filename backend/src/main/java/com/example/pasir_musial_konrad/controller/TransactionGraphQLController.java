package com.example.pasir_musial_konrad.controller;

import com.example.pasir_musial_konrad.dto.BalanceDto;
import com.example.pasir_musial_konrad.dto.TransactionDTO;
import com.example.pasir_musial_konrad.model.Transaction;
import com.example.pasir_musial_konrad.model.User;
import com.example.pasir_musial_konrad.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TransactionGraphQLController {
    private final TransactionService transactionService;

    public TransactionGraphQLController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @MutationMapping
    public Transaction addTransaction(@Valid @Argument TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    @MutationMapping
    public Transaction updateTransaction(
            @Argument Long id,
            @Valid @Argument TransactionDTO transactionDTO
            ){
        return transactionService.updateTransaction(id, transactionDTO);
    }

    @MutationMapping
    public void deleteTransaction(@Argument Long id) {
       transactionService.deleteTransaction(id);
    }

    @QueryMapping
    public BalanceDto userBalance() {
        User user = transactionService.getCurrentUser();
        return transactionService.getUserBalance(user);
    }
}
