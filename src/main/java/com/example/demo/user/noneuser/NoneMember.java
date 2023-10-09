package com.example.demo.user.noneuser;

import com.example.demo.user.defaultuser.DefaultMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Getter
public class NoneMember extends DefaultMember {
    private String ip;

}

