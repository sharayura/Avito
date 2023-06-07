package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private int author;
    private String authorImage;
    private String authorFirstName;
    private String createdAt;
    private int pk;
    private String text;
}
