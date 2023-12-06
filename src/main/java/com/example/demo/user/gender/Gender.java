package com.example.demo.user.gender;

import com.example.demo.security.jwt.Trans;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Gender {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private Trans trans;

    public Gender(Trans trans) {
        this.trans = trans;
    }
}
