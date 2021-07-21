package com.latte.server.post.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY_TB")
@Getter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String category;
    private String kind;
}
