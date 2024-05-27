package com.example.jobportal.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//once logged in successfully
// retrieve the user object
//check the roles for the user
//if jobseeker or recruiter role then send them to dashboard page
@Component
public class CustomAuhenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        boolean hasJobSeekerRole = authentication.getAuthorities().stream().anyMatch(
                r -> r.getAuthority().equals("Job Seeker")
        );
        boolean hasRecruiterRole = authentication.getAuthorities().stream().anyMatch(
                r -> r.getAuthority().equals("Recruiter")
        );

        if (hasRecruiterRole || hasJobSeekerRole){
            response.sendRedirect("/dashboard/");
        }

    }
}
















