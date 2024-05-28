package com.example.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints =  {
        @UniqueConstraint(columnNames = {"userId","job"})
})
public class JobSeekerSave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private JobSeekerProfile userId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job")
    private JobPostActivity job;


}













