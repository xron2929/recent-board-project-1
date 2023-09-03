package com.example.demo.user.siteuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteMemberRepository extends JpaRepository<SiteMember,Long> {
    @Override
    Optional<SiteMember> findById(Long aLong);
    Optional<SiteMember> findByUserId(String userId);
}
