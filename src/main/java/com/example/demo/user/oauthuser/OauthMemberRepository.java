package com.example.demo.user.oauthuser;

import com.example.demo.user.oauthuser.OauthMember;
import com.example.demo.user.siteuser.SiteMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthMemberRepository extends JpaRepository<OauthMember,Long> {
    Optional<OauthMember> findByUserId(String userId);
}
