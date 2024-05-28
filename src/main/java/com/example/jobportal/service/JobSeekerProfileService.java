package com.example.jobportal.service;

import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.JobSeekerProfileRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UserRepository userRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.userRepository = userRepository;
    }

    public Optional<JobSeekerProfile> getOne(Integer id){
        return jobSeekerProfileRepository.findById(id);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile){
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            User user = userRepository.findByEmail(currentUsername).orElseThrow(()->new UsernameNotFoundException("User not found"));

            Optional<JobSeekerProfile> jobSeekerProfile = getOne(user.getUserId());
            return jobSeekerProfile.orElse(null);
        }else {
            return null;
        }
    }
}
























