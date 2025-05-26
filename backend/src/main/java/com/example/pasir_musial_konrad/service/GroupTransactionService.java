package com.example.pasir_musial_konrad.service;

import com.example.pasir_musial_konrad.dto.GroupTransactionDTO;
import com.example.pasir_musial_konrad.model.Debt;
import com.example.pasir_musial_konrad.model.Group;
import com.example.pasir_musial_konrad.model.Membership;
import com.example.pasir_musial_konrad.model.User;
import com.example.pasir_musial_konrad.repository.DebtRepository;
import com.example.pasir_musial_konrad.repository.GroupRepository;
import com.example.pasir_musial_konrad.repository.MembershipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupTransactionService {

    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final DebtRepository debtRepository;

    public GroupTransactionService(GroupRepository groupRepository, MembershipRepository membershipRepository, DebtRepository debtRepository) {
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.debtRepository = debtRepository;
    }

    public void addGroupTransaction(GroupTransactionDTO dto, User currentUser) {
        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy"));

        List<Membership> members = membershipRepository.findByGroupId(group.getId());
        List<Long> selectedUserIds = dto.getSelectedUserIds();

        if (selectedUserIds == null || selectedUserIds.isEmpty()) {
            throw new IllegalArgumentException("Nie wybrano uzytkownikow!");
        }

        double amountPerUser = dto.getAmount() / selectedUserIds.size();

        for(Membership member : members) {
            User debtor = member.getUser();
            if (!debtor.getId().equals(currentUser.getId()) && selectedUserIds.contains(debtor.getId())) {
                Debt debt = new Debt();
                debt.setDebtor(debtor);
                debt.setCreditor(currentUser);
                debt.setGroup(group);
                debt.setAmount(amountPerUser);
                debt.setTitle(dto.getTitle());
                debtRepository.save(debt);
            }
        }


    }
}
