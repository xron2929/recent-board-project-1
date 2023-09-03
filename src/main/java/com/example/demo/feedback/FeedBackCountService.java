package com.example.demo.feedback;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Board,BoardId를 String 값으로 입력후 리턴 값을 likeCount ex) board-1 -> board의 1번째에 있는 좋아요 싫어요 값 출력
// userId에 특수문자 방지 달아서 Redis랑 충돌 X
// 만약 있으면 그거 리턴
// 없으면 FeedBackService에서 Like랑 DisLikeCount 한번에 받기
// 그거 받은 다음에 만약 그 값이 100개가 넘으면 0.1k로 리턴 할 것이므로
// 할 수 있는데 더 빠르다는 보장이 없어서 간단하게 작성 예정
@Service
public class FeedBackCountService {
    public FeedBackCountService(UserLikeRepository likeRepository, DisLikeRepository disLikeRepository,
                                LikeDslRepository likeDslRepository, UserDisLikeDslRepository disLikeDslRepository) {
        this.likeRepository = likeRepository;
        this.disLikeRepository = disLikeRepository;
        this.likeDslRepository = likeDslRepository;
        this.disLikeDslRepository = disLikeDslRepository;
    }

    private UserLikeRepository likeRepository;
    private DisLikeRepository disLikeRepository;
    private LikeDslRepository likeDslRepository;
    private UserDisLikeDslRepository disLikeDslRepository;
    @Transactional
    public FeedBackCount getFeedBackCount(JoinStatus joinStatus,Long joinId) {
        return FeedBackCount.builder()
                .likeCount(likeDslRepository.getLikeCount(joinStatus,joinId).toString())
                .disLikeCount(disLikeDslRepository.getDisLikeCount(joinStatus,joinId).toString())
                .build();
    }
    @Transactional
    public Long isExistenceDisLike(JoinStatus joinStatus,Long joinId,String userId) {
        boolean existanceDisLikeFlag = disLikeDslRepository.isExistanceDisLike(joinStatus, joinId, userId);
        Long disLikeCount = disLikeDslRepository.getDisLikeCount(joinStatus, joinId);
        if(existanceDisLikeFlag==true) {
            disLikeDslRepository.deleteDisLikeAccount(joinStatus,joinId,userId);
            return disLikeCount-1;
        }
        UserDisLike userDisLike = UserDisLike.builder()
                .joinStatus(joinStatus)
                .joinId(joinId)
                .userId(userId).build();
        disLikeRepository.save(userDisLike);
        return disLikeCount+1;
    }
    @Transactional
    public Long isExistenceLike(JoinStatus joinStatus,Long joinId,String userId) {
        System.out.println("joinStatus.name() = " + joinStatus.name());
        boolean existanceLikeFlag = likeDslRepository.isExistanceDisLike(joinStatus, joinId, userId);
        Long likeCount = likeDslRepository.getLikeCount(joinStatus, joinId);
        System.out.println("FeedBackCountAPi - likeCount = " + likeCount);
        if(existanceLikeFlag==true) {
            likeDslRepository.deleteLikeAccount(joinStatus,joinId,userId);
            return likeCount-1;
        }
        UserLike like = UserLike.builder()
                .joinStatus(joinStatus)
                .joinId(joinId)
                .userId(userId).build();
        likeRepository.save(like);
        return likeCount+1;
    }

}
