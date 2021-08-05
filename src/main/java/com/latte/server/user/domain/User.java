package com.latte.server.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USER_TB")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private User(String userName, String userId, String email, String intro) {
        this.userName = userName;
        this.userId = userId;
        this.email = email;
        this.intro = intro;
    }

    // == 생성 메서드 == //
    public static User createTestUser(String userName, String userId, String email, String intro){
        return new User(userName, userId, email, intro);
    }
}
