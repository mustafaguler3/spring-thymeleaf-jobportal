package com.example.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints =  {
        @UniqueConstraint(columnNames = {"userId","job"})
})
public class JobSeekerApply implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private JobSeekerProfile userId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job")
    private JobPostActivity job;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date applyDate;
    private String coverLetter;
}





















