package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String filePath;

    private long fileSize;

    private String mediaType;

    @Lob
    private byte[] data;

    @OneToOne
    private User user;
}
