package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private long fileSize;

    private String mediaType;

    @Lob
    private byte[] data;

}
