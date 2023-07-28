package com.example.demo.user.noneuser;


import com.example.demo.user.noneuser.NoneMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoneMemberRepository extends JpaRepository<NoneMember,Long> {
}
