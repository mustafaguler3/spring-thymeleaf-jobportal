package com.example.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users_type")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userTypeName;

    @OneToMany(mappedBy = "userType",cascade = CascadeType.ALL)
    private List<User> users;
}
















