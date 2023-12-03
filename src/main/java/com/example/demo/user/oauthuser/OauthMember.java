package com.example.demo.user.oauthuser;

import com.example.demo.user.gender.Gender;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
// @DiscriminatorValue("OAUTH") // 엔티티를 저장
public class OauthMember extends DefaultMember {

    private String phoneNumber;
    private long age;
    @Setter private String email;
    @OneToOne(cascade = CascadeType.ALL)
    private Gender gender;



}

