package com.example.pasir_musial_konrad.service;

import com.example.pasir_musial_konrad.dto.DebtDTO;
import com.example.pasir_musial_konrad.model.*;
import com.example.pasir_musial_konrad.repository.DebtRepository;
import com.example.pasir_musial_konrad.repository.GroupRepository;
import com.example.pasir_musial_konrad.repository.TransactionRepository;
import com.example.pasir_musial_konrad.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService {

    private final DebtRepository debtRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public DebtService(DebtRepository debtRepository, GroupRepository groupRepository, UserRepository userRepository, TransactionService transactionService, TransactionRepository transactionRepository) {
        this.debtRepository = debtRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    public List<Debt> getGroupDebts(Long groupId) {
        return debtRepository.findByGroupId(groupId);
    }

    public Debt createDebt(DebtDTO debtDTO) {
        Group group = groupRepository.findById(debtDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy o id " + debtDTO.getGroupId()));

        User debtor = userRepository.findById(debtDTO.getDebtorId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono dluznika o id " + debtDTO.getDebtorId()));

        User creditor = userRepository.findById(debtDTO.getCreditorId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono wierzyciela o id " + debtDTO.getCreditorId()));

        Debt debt = new Debt();
        debt.setGroup(group);
        debt.setCreditor(creditor);
        debt.setDebtor(debtor);
        debt.setAmount(debtDTO.getAmount());
        debt.setTitle(debtDTO.getTitle());

        return debtRepository.save(debt);
    }

    public void deleteDebt(Long debtId, User currentUser) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new EntityNotFoundException("Dług o id: " + debtId + " nie istnieje"));

        if (!debt.getCreditor().getId().equals(currentUser.getId())) {
            throw new SecurityException("Tylko wierzyciel moze usunac ten dług");
        }

        debtRepository.delete(debt);
    }

    public boolean markAsPaid(Long debtId, User user) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znalezniono długu"));

        if (!debt.getDebtor().getId().equals(user.getId())) {
            throw new SecurityException("Nie jesteś dluznikiem");
        }

        debt.setMarkedAsPaid(true);
        debtRepository.save(debt);
        return true;
    }

    public boolean confirmPayment(Long debtId, User user) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono długu"));

        if(!debt.getCreditor().getId().equals(user.getId())) {
            throw new SecurityException("Nie jesteś wierzycielem!");
        }

        if(!debt.isMarkedAsPaid()){
            throw new IllegalStateException("Dłużnik nie oznaczył jako opłacone!");
        }

        debt.setConfirmedByCreditor(true);
        debtRepository.save(debt);

        Transaction incomeTx = new Transaction(
                debt.getAmount(),
                TransactionType.INCOME,
                "Spłata długu",
                "Spłata długu od: " + debt.getDebtor().getEmail(),
                debt.getCreditor()
        );

        transactionRepository.save(incomeTx);

        Transaction expenseTx = new Transaction(
                debt.getAmount(),
                TransactionType.EXPENSE,
                "Spłata długu",
                "Spłacono dług dla: " + debt.getCreditor().getEmail(),
                debt.getDebtor()
        );
        transactionRepository.save(expenseTx);
        return true;


    }
}
