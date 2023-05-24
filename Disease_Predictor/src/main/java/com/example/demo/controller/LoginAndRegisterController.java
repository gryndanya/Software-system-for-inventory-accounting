package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginAndRegisterController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showSignUpForm(Model model){
        model.addAttribute("user",new User());

        return "login/signup_form";
    }

    @PostMapping("/process_register")
    public String processRegistration(Model model, User user) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if(repo.findByEmail(user.getEmail())!=null) {
            model.addAttribute("error","An account already exists.");
            return "login/register_bad";
        }else{
            user.setRole("USER");
            repo.save(user);
            return "login/register_success";
        }
    }

    @PostMapping("/adminLogin")
    public String showAdminLogin(Model model){
        return "login/admin_login";
    }

}

