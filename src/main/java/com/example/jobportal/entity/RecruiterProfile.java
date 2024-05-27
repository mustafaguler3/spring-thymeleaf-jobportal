package com.example.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class RecruiterProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

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

    @Transient
    public String getPhotosImagePath(){
        if (profilePhoto == null) return null;
        return "/photo/recruiter/"+id + "/"+profilePhoto;
    }
}














