package com.example.demo.user.oauthuser;

import com.example.demo.user.oauthuser.OauthMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthMemberRepository extends JpaRepository<OauthMember,Long> {
}
