package com.example.pasir_musial_konrad.service;

import com.example.pasir_musial_konrad.dto.GroupDTO;
import com.example.pasir_musial_konrad.model.Group;
import com.example.pasir_musial_konrad.model.Membership;
import com.example.pasir_musial_konrad.model.User;
import com.example.pasir_musial_konrad.repository.DebtRepository;
import com.example.pasir_musial_konrad.repository.GroupRepository;
import com.example.pasir_musial_konrad.repository.MembershipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipService membershipService;
    private final DebtRepository debtRepository;

    public GroupService(GroupRepository groupRepository, MembershipRepository membershipRepository,
                        MembershipService membershipService, DebtRepository debtRepository){
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.membershipService = membershipService;
        this.debtRepository = debtRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @MutationMapping
    public Group createGroup(GroupDTO groupDTO) {
        User owner = membershipService.getCurrentUser();
        Group group = new Group();
        group.setName(groupDTO.getName());
        group.setOwner(owner);
        Group savedGroup = groupRepository.save(group);
        Membership membership = new Membership();
        membership.setUser(owner);
        membership.setGroup(savedGroup);
        membershipRepository.save(membership);
        return savedGroup;
    }

    public void deleteGroup(Long id) {

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grupa o id: " + id + " nie istnieje"));

        debtRepository.deleteAll(debtRepository.findByGroupId(id));
        membershipRepository.deleteAll(membershipRepository.findByGroupId(id));

        groupRepository.delete(group);
    }
}
