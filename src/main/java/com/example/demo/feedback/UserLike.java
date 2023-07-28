package com.example.demo.feedback;

import com.example.demo.baseTime.BaseTimeEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"join_status", "join_id","user_id"}))
public class UserLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private String userId;
    @Enumerated
    @Column(name="join_status")
    private JoinStatus joinStatus;
    @Column(name = "join_id")
    private Long joinId;
    @Builder
    public UserLike(@NonNull String userId, @NonNull JoinStatus joinStatus, @NonNull Long joinId) {
        this.userId = userId;
        this.joinStatus = joinStatus;
        this.joinId = joinId;
    }

}
