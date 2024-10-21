package com.khanjabir.rak.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    @Entity
    @Table(name = "USERS")
    @Getter @Setter @ToString
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        @Size(max = 50)
        private String name;

        @NotBlank
        @Email
        @Column(unique = true)
        private String email;

        @NotBlank
        @Size(min = 8)
        @JsonIgnore // This will prevent the password field from being included in JSON responses
        private String password;

    }
