package com.example.pasir_musial_konrad.repository;

import com.example.pasir_musial_konrad.model.Transaction;
import com.example.pasir_musial_konrad.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);

    List<Transaction> findByUser(User user);
}
