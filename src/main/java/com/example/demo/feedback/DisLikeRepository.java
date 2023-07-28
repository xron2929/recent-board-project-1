package com.example.demo.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisLikeRepository extends JpaRepository<UserDisLike,Long> {
}
