package com.example.demo.board;

import com.example.demo.boradAndUser.BoardSearchDataDto;
import com.example.demo.boradAndUser.UserJoinRepository;
import com.example.demo.user.UserDslRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardSearchService {

    @Autowired
    BoardDslRepository boardJpaRepository;
    @Autowired
    UserJoinRepository userJoinRepository;
    @Autowired
    BoardPageCalculator boardPageCalculator;
    @Transactional
    public List<BoardSearchDataDto> boardSearch(Long pageQuantity, Long boardQuantity, String keyword){
        return userJoinRepository.selectFindById(pageQuantity-1,boardQuantity,"%"+keyword+"%");
    }
    @Transactional
    public BoardPageApiDTO boardSearchPage (Long currentPageQuantity,Long boardQuantity,String keyword){
        Long count = boardJpaRepository.selectFindByIdPage("%" + keyword + "%");
        System.out.println("count = " + count);
        return boardPageCalculator.calculate(count,currentPageQuantity,boardQuantity);
    }
}
