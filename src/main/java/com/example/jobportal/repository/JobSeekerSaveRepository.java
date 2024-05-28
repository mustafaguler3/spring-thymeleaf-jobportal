package com.example.jobportal.repository;

import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave,Integer> {
    List<JobSeekerSave> findByUserId(JobSeekerProfile userAccountId);
    List<JobSeekerSave> findByJob(JobPostActivity job);
}
