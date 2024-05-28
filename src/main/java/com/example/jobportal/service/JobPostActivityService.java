package com.example.jobportal.service;

import com.example.jobportal.entity.*;
import com.example.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity){
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobs> getRecruiterJobs(int recruiter){
        List<IRecruiterJobs> recruiterJobs = jobPostActivityRepository.getRecruiterJobs(recruiter);

        List<RecruiterJobs> recruiterJobsList = new ArrayList<>();

        for (IRecruiterJobs rec : recruiterJobs){
            JobLocation loc = new JobLocation();
            loc.setId(rec.getLocationId());
            loc.setCity(rec.getCity());
            loc.setCountry(rec.getCountry());
            loc.setState(rec.getState());

            JobCompany com = new JobCompany();
            com.setId(rec.getCompanyId());
            com.setLogo("");
            com.setName(rec.getName());

            recruiterJobsList.add(new RecruiterJobs(rec.getTotalCandidates(),rec.getJob_post_id(),
                    rec.getJob_title(),loc,com));
        }

        return recruiterJobsList;
    }

    public JobPostActivity getOne(int id){
        return jobPostActivityRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }
}


















