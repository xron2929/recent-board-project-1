package com.example.demo.alarm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Object> {
    Page<Alarm> findByBoardWriterId(String boardWriterId, Pageable pageable);
    List<Alarm> findByBoardWriterId(String boardWriterId);


}
