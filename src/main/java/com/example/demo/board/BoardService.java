package com.example.demo.board;


import com.example.demo.entityjoin.*;
import com.example.demo.user.MemberBoardQueryDTO;
import com.example.demo.user.UserIdAndPasswordDto;
import com.example.demo.user.UserJoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
// Repository를 추상화 계층 <-> 구현체 계층 분리하고
// 구현체 계층에는 entity를 기반으로 repo 설정
// 추상화 계층(도메인)에는 url api 단위로 개발하기
// 서비스 단에서는 그걸 호출 하고
// 그 옆단에는 이제 util단 따로 처리

@Service
@Transactional
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired private BoardDslRepository boardJpaRepository;
    @Autowired private JoinDslRepository joinDslRepository;
    @Autowired private DeleteBoardRepository deleteBoardRepository;
    @Autowired private BoardMapper boardMapper;
    @Autowired
    UserJoinRepository userJoinRepository;
    @Autowired
    BoardPageCalculator boardPageCalculator;
    // 모든 board 조회랑 board 삭제된거랑 동시성 그냥 다 보장하려고 이렇게 함

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Board findAllByImageId(Long id) throws Exception {
        System.out.println("id = " + id);
        Board findBoard =  boardRepository.findById(id).orElse(null);
        return findBoard;
    }
    public String findBoardUserUUID(Long id) throws Exception {
        System.out.println("id = " + id);
        String findUserUUID = userJoinRepository.findNoneMemberByBoardId(id);
        if(findUserUUID == null) {
            findUserUUID = userJoinRepository.findDefaultMemberByBoardId(id);
        }
        System.out.println("findUserUUID = " + findUserUUID);
        return findUserUUID;

    }
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }
    public List<Board> saveAll(List<Board> boards) {
        return boardRepository.saveAll(boards);
    }
    @Transactional(readOnly = true,propagation = Propagation.REQUIRES_NEW)
    public NoneUserBoardSaveDataDto findNoneUserPasswordBoard(Long id) {
        return joinDslRepository.findNoneUserEditBoard(id);
    }
    public UserBoardSaveDataDto findUserPasswordBoard(Long id) {
        return joinDslRepository.findUserEditBoard(id);
    }

    @Transactional
    public void updateBoard(Board board) {
        boardJpaRepository.updateBoard(board.getId(), board.getTitle(),board.getContent());
    }
    // 현재 페이지 정보 2면 2뿌려주면 됨
    // 이걸 js vs 백엔드
    // 백엔드 위주로 개발하되, js는 익숙하긴 한데 자바가 더 쉬우니까 자바 쓰는게 맞을듯
    public Long findBoardOnlyCount() {
        return boardJpaRepository.findBoardCount();
    }

    public BoardPageApiDTO  findBoardCount(Long currentPageQuantity, Long boardQuantity) {
        long count = boardJpaRepository.findBoardCount();
        System.out.println("//");
        return boardPageCalculator.calculate(count,currentPageQuantity,boardQuantity);
    }

    public List<Long> findDeleteBoards() {
        return null;
    }
    public void updateBoard(Long boardId, String title, String content) {
        boardJpaRepository.updateBoard(boardId,title,content);
    }
    public UserIdAndPasswordDto findUserIdAndPassword(Long boardId) {
        return userJoinRepository.findUserIdAndPassword(boardId);
    }
    public String getUserAuthority(Long boardId) {
        return joinDslRepository.getAuthority(boardId);
    }
    public TitleAndUserIdDto findUserDto(Long boardId) {
        System.out.println("error1");
        TitleAndUserIdDto titleByBoardId = joinDslRepository.findUserDto(boardId);
        System.out.println("titleByBoardId = " + titleByBoardId);
        return titleByBoardId;
    }
    public NoneUserUuidANdTitleAndPasswordDto findNoneUserDto(Long boardId) {
        System.out.println("error2");
        NoneUserUuidANdTitleAndPasswordDto noneUserDto =
                joinDslRepository.findNoneuserDto(boardId);
        System.out.println("titleByBoardId = " + noneUserDto);
        return noneUserDto;
    }
    public BoardResponseDto getBoardResponseDto(Long boardId) {
        return joinDslRepository.getBoardResponseDto(boardId);
    }
    public Long getFinalBoardId() {
        Board board = new Board(true);
        Board saveBoard = boardRepository.save(board);
        System.out.println("getFinalBoardId - finalBoardId = " + saveBoard.getId());
        return saveBoard.getId();
    }

}


