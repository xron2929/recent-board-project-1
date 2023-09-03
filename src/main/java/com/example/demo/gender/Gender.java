package com.example.demo.gender;

import com.example.demo.security.jwt.Trans;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
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
