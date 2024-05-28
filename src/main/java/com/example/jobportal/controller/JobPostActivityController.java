package com.example.jobportal.controller;

import com.example.jobportal.entity.*;
import com.example.jobportal.service.JobPostActivityService;
import com.example.jobportal.service.JobSeekerApplyService;
import com.example.jobportal.service.JobSeekerSaveService;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {

    private final UserService userService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobPostActivityController(UserService userService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.userService = userService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @GetMapping("/dashboard/")
    public String searchJobs(@RequestParam(value = "job",required = false) String job,
                             @RequestParam(value = "location",required = false) String location,
                             @RequestParam(value = "partTime",required = false) String partTime,
                             @RequestParam(value = "fullTime",required = false) String fullTime,
                             @RequestParam(value = "freelance",required = false) String freelance,
                             @RequestParam(value = "remoteOnly",required = false) String remoteOnly,
                             @RequestParam(value = "officeOnly",required = false) String officeOnly,
                             @RequestParam(value = "partialRemote",required = false) String partialRemote,
                             @RequestParam(value = "today",required = false) boolean today,
                             @RequestParam(value = "days7",required = false) boolean days7,
                             @RequestParam(value = "days30",required = false) boolean days30,
                             Model model){

        model.addAttribute("partTime", Objects.equals(partTime,"Part-Time"));
        model.addAttribute("fullTime", Objects.equals(partTime,"Full-Time"));
        model.addAttribute("freelance", Objects.equals(partTime,"Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(partTime,"Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(partTime,"Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partTime,"Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if (days30){
            searchDate = LocalDate.now().minusDays(30);
        }else if (days7){
            searchDate = LocalDate.now().minusDays(7);
        }else {
            dateSearchFlag = false;
        }

        if (partTime == null && fullTime == null && freelance == null){
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null){
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }


        Object currentUserProfile = userService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            model.addAttribute("username",currentUsername);
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                List<RecruiterJobs> recruiterJobs = jobPostActivityService.getRecruiterJobs(((RecruiterProfile) currentUserProfile).getId());

                model.addAttribute("jobPost",recruiterJobs);
            }else {
                List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidateJobs((JobSeekerProfile) currentUserProfile);
                List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);

                boolean exist;
                boolean saved;

                for (JobPostActivity jobPostActivity : jobPost){
                    exist = false;
                    saved = false;

                    for (JobSeekerApply jobSeekerApply : jobSeekerApplyList){
                        if (Objects.equals(jobPostActivity.getJobPostId(),jobSeekerApply.getJob().getJobPostId())){
                            jobPostActivity.setActive(true);
                            exist = true;
                            break;
                        }
                    }

                    for (JobSeekerSave jobSeekerSave : jobSeekerSaveList){
                        if (Objects.equals(jobPostActivity.getJobPostId(),jobSeekerSave.getJob().getJobPostId())){
                            jobPostActivity.setActive(true);
                            saved = true;
                            break;
                        }
                    }

                    if (!exist){
                        jobPostActivity.setIsActive(false);
                    }
                    if (!saved){
                        jobPostActivity.setSaved(false);
                    }

                    model.addAttribute("jobPost",jobPost);
                }
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

    @PostMapping("dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int id,
                          Model model){
        JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
        model.addAttribute("jobPostActivity",jobPostActivity);
        model.addAttribute("user",userService.getCurrentUserProfile());
        return "add-jobs";
    }
}














