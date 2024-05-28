package com.example.jobportal.controller;

import com.example.jobportal.entity.JobPostActivity;
import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.JobSeekerSave;
import com.example.jobportal.entity.User;
import com.example.jobportal.service.JobPostActivityService;
import com.example.jobportal.service.JobSeekerProfileService;
import com.example.jobportal.service.JobSeekerSaveService;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {

    private final UserService userService;
    private final JobSeekerProfileService jobSeekerProfileService;
    private final JobPostActivityService jobPostActivityService;
    private JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobSeekerSaveController(UserService userService, JobSeekerProfileService jobSeekerProfileService, JobPostActivityService jobPostActivityService, JobSeekerSaveService jobSeekerSaveService) {
        this.userService = userService;
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id") int id, JobSeekerSave jobSeekerSave){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String currentUsername = authentication.getName();
            User user = userService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);

            if (seekerProfile.isPresent() && jobPostActivity != null){
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(seekerProfile.get());
            }else {
                throw new RuntimeException("User not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard/";
    }
    @GetMapping("saved-jobs/")
    public String saveJobs(Model model){
        List<JobPostActivity> jobPost = new ArrayList<>();
        Object currentUserProfile = userService.getCurrentUserProfile();

        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
        for (JobSeekerSave jobSeekerSave : jobSeekerSaveList){
            jobPost.add(jobSeekerSave.getJob());
        }
        model.addAttribute("jobPost",jobPost);
        model.addAttribute("user",currentUserProfile);
        return "saved-jobs";
    }
}














