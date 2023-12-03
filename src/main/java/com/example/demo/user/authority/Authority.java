package com.example.demo.user.authority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authority",
        indexes = @Index(name = "authority_index",
                columnList = "authority_name"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements Serializable {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}
