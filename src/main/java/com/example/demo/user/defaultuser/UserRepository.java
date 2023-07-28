package com.example.demo.user.defaultuser;

import com.example.demo.user.defaultuser.DefaultMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<DefaultMember,Long> {
    public List<DefaultMember> findByUserId(String userId);
}
