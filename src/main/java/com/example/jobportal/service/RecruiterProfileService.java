package com.example.jobportal.service;

import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.RecruiterProfileRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UserRepository userRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    public Optional<RecruiterProfile> getOne(Integer id){
        return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile addNew(RecruiterProfile recruiterProfile){
        return recruiterProfileRepository.save(recruiterProfile);
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            User user = userRepository.findByEmail(currentUsername).orElseThrow(()->new UsernameNotFoundException("User not found"));

            Optional<RecruiterProfile> recruiterProfile = getOne(user.getUserId());
            return recruiterProfile.orElse(null);
        }else {
            return null;
        }
    }
}

















