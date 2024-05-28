package com.example.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class JobPostActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer jobPostId;

    @ManyToOne
    @JoinColumn(name = "postedById")
    private User postedById;

    @ManyToOne
    @JoinColumn(name = "jobLocationId")
    private JobLocation jobLocationId;

    @ManyToOne
    @JoinColumn(name = "jobCompanyId")
    private JobCompany jobCompanyId;
    @Transient
    private Boolean isActive;
    @Transient
    private Boolean isSaved;
    @Length(max = 10000)
    private String descriptionOfJob;

    private String jobType;
    private String salary;
    private String remote;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;
    private String jobTitle;
}














