package com.example.demo.userAuthority;


import com.example.demo.authority.Authority;
import com.example.demo.user.defaultuser.DefaultMember;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthority implements Serializable {

    @Override
    public String toString() {
        return "UserAuthority{" +
                "id=" + id +
                ", authority=" + authority +
                '}';
    }

    @Id
    @Column(name = "user_authority_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private DefaultMember userId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="authority_name")
    private Authority authority;
    public Long getId() {
        return id;
    }

    public DefaultMember getUserId() {
        return userId;
    }


    public UserAuthority(Authority authority) {
        this.authority = authority;
    }

    public Authority getAuthority() {
        return authority;
    }
}
