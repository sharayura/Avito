package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long createdAt;
    private String text;

    @ManyToOne
    private User user;

    @ManyToOne
    private Ad ad;

}
