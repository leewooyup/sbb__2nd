package com.ll.exam.sbb.user;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
}
