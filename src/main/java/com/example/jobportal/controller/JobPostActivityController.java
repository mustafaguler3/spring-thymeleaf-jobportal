package com.example.jobportal.controller;

import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.RecruiterJobs;
import com.example.jobportal.entity.RecruiterProfile;
import com.example.jobportal.entity.User;
import com.example.jobportal.service.JobPostActivityService;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
public class JobPostActivityController {

    private final UserService userService;
    private final JobPostActivityService jobPostActivityService;

    @Autowired
    public JobPostActivityController(UserService userService, JobPostActivityService jobPostActivityService) {
        this.userService = userService;
        this.jobPostActivityService = jobPostActivityService;
    }

    @GetMapping("/dashboard/")
    public String searchJobs(Model model){
        Object currentUserProfile = userService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            model.addAttribute("username",currentUsername);
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                List<RecruiterJobs> recruiterJobs = jobPostActivityService.getRecruiterJobs(((RecruiterProfile) currentUserProfile).getId());

                model.addAttribute("jobPost",recruiterJobs);
            }
        }
        model.addAttribute("user",currentUserProfile);

        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model){
        model.addAttribute("jobPostActivity",new JobPostActivity());
        model.addAttribute("user",userService.getCurrentUserProfile());
        return "add-jobs";
    }
    @PostMapping("/dashboard/addNew")
    public String addNew(JobPostActivity jobPostActivity,
                         Model model){
        User user = userService.getCurrentUser();
        if (user != null){
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity",jobPostActivity);
        jobPostActivityService.addNew(jobPostActivity);

        return "redirect:/dashboard/";
    }
}














