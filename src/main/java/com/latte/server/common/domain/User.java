package com.latte.server.common.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USER_TB")
@Getter
public class User {

    @Id
    @GeneratedValue
    @Column(name = "uid")
    private Long id;

    private String image;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_password")
    private String userPassword;

    private String salt;

    @Column(name = "login_by")
    private String loginBy;

    private String email;
    private int phone;
    private String intro;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private int isDeleted;

}
