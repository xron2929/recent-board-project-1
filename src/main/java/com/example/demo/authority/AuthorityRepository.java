package com.example.demo.authority;

import com.example.demo.authority.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
