package com.example.jobportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class RecruiterProfile {

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String company;
    @Column(nullable = true)
    private String profilePhoto;

    public RecruiterProfile(User user) {
        this.user = user;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}














