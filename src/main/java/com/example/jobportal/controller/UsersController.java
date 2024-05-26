package com.example.jobportal.controller;

import com.example.jobportal.entity.User;
import com.example.jobportal.entity.UserType;
import com.example.jobportal.service.UserService;
import com.example.jobportal.service.UserTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsersController {

    private final UserTypeService userTypeService;
    private final UserService userService;

    @Autowired
    public UsersController(UserTypeService userTypeService, UserService userService) {
        this.userTypeService = userTypeService;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model){
        List<UserType> userTypes = userTypeService.getAll();
        model.addAttribute("getAllTypes",userTypes);
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegister(@Valid User user){
        userService.addNew(user);
        return "dashboard";
    }
}

















