package com.example.demo.user.noneuser;


import com.example.demo.user.noneuser.NoneMember;
import com.example.demo.user.siteuser.SiteMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoneMemberRepository extends JpaRepository<NoneMember,Long> {
    Optional<NoneMember> findByUserId(String userId);
}
