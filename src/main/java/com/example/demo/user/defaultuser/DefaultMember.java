package com.example.demo.user.defaultuser;


import com.example.demo.board.Board;
import com.example.demo.user.userAuthority.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Slf4j
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED) // 상속 구현 전략 선택
public class DefaultMember implements Serializable {
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", ModifiedAt=" + ModifiedAt +

                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String userId;
    protected String nickname;
    protected String password;

    @LastModifiedDate
    protected LocalDateTime ModifiedAt;

    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    protected List<UserAuthority> userAuthorities;

    @OneToMany(mappedBy = "member")
    @BatchSize(size = 1000)
    protected List<Board> boards = new ArrayList<>();

    // @OneToMany(mappedBy = "member")
    // protected List<ParentComment> Comments = new ArrayList<>();




}

