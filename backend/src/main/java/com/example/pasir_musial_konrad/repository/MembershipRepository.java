package com.example.pasir_musial_konrad.repository;

import com.example.pasir_musial_konrad.model.Membership;
import com.example.pasir_musial_konrad.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findByGroupId(Long groupId);
}
