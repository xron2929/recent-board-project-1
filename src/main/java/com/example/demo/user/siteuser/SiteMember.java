package com.example.demo.user.siteuser;


import com.example.demo.gender.Gender;
import com.example.demo.security.jwt.Trans;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class SiteMember extends DefaultMember {
    @Override
    public String toString() {
        return "SiteMember{" +
                "id='" + id + '\'' +
                "password='" + password + '\'' +
                "nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }


    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    private Gender gender;
}

