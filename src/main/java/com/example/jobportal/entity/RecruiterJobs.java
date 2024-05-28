package com.example.jobportal.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecruiterJobs {

    private Long totalCandidates;
    private Integer jobPostId;
    private String jobTitle;
    private JobLocation jobLocationId;
    private JobCompany jobCompanyId;

    public RecruiterJobs(Long totalCandidates, int jobPostId, String jobTitle, JobLocation loc, JobCompany com) {
        this.totalCandidates = totalCandidates;
        this.jobPostId = jobPostId;
        this.jobTitle = jobTitle;
        this.jobLocationId = loc;
        this.jobCompanyId = com;
    }
}
