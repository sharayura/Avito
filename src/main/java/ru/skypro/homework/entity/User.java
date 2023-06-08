package ru.skypro.homework.entity;

import ru.skypro.homework.dto.Role;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String password;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private Image image;

}
