package com.example.jobportal.controller;

import com.example.jobportal.entity.JobSeekerProfile;
import com.example.jobportal.entity.Skill;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.UserRepository;
import com.example.jobportal.service.JobSeekerProfileService;
import com.example.jobportal.utils.FileDownloadUtil;
import com.example.jobportal.utils.FileUploadUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private UserRepository userRepository;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, UserRepository userRepository) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String jobSeekerProfile(Model model){
        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Skill> skills = new ArrayList<>();

        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());

            if (seekerProfile.isPresent()){
                jobSeekerProfile = seekerProfile.get();
                if (jobSeekerProfile.getSkills().isEmpty()){
                    skills.add(new Skill());
                    jobSeekerProfile.setSkills(skills);
                }
            }

            model.addAttribute("skills",skills);
            model.addAttribute("profile",jobSeekerProfile);
        }
        return "job-seeker-profile";
    }
    @PostMapping("/addNew")
    public String addNew(JobSeekerProfile jobSeekerProfile,
                         @RequestParam("image")MultipartFile image,
                         @RequestParam("pdf") MultipartFile pdf,
                         Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("User not found"));
            jobSeekerProfile.setUser(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
        }

        List<Skill> skillList = new ArrayList<>();
        model.addAttribute("profile",jobSeekerProfile);
        model.addAttribute("skills",skillList);

        for (Skill skill : jobSeekerProfile.getSkills()){
            skill.setJobSeekerProfile(jobSeekerProfile);
        }

        String imageName = "";
        String resumeName = "";

        if (!Objects.equals(image.getOriginalFilename(),"")){
            imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setProfilePhoto(imageName);
        }

        if (!Objects.equals(pdf.getOriginalFilename(),"")){
            resumeName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }

        JobSeekerProfile seekerProfile = jobSeekerProfileService.addNew(jobSeekerProfile);

        try {
            String uploadDir = "/photos/candidate/"+jobSeekerProfile.getUserAccountId();
            if (!Objects.equals(image.getOriginalFilename(),"")){
                FileUploadUtil.saveFile(uploadDir,imageName,image);
            }
            if (!Objects.equals(pdf.getOriginalFilename(),"")){
                FileUploadUtil.saveFile(uploadDir,resumeName,pdf);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id,Model model){
        Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(id);
        model.addAttribute("profile",seekerProfile.get());
        return "job-seeker-profile";
    }
    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName,
                                            @RequestParam(value = "userID") String userId){
        FileDownloadUtil fileDownloadUtil = new FileDownloadUtil();
        Resource resource = null;

        try {
            resource =fileDownloadUtil.getFileAsResource("photos/candidate/"+userId,fileName);
        }catch (IOException e){
            return ResponseEntity.badRequest().build();
        }

        if (resource == null){
            return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
        }
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\""+resource.getFilename()+"\"";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }
}






















