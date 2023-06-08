package ru.skypro.homework.entity;

import javax.persistence.*;

@Entity
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private int price;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne
    private Image image;

}