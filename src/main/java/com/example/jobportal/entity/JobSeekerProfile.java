package com.example.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class JobSeekerProfile {

    @Id
    private Integer userAccountId;

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String workAuthorization;
    private String employmentType;
    private String resume;
    private String profilePhoto;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "jobSeekerProfile")
    private List<Skill> skills;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public JobSeekerProfile(User user) {
        this.user = user;
    }

    public String getPhotosImagePath(){
        if (profilePhoto == null || userAccountId == null){//we changed int value to Integer
            return null;
        }
        return "/photos/candidate/"+ userAccountId+"/"+profilePhoto;
    }

}














