package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainPageController {

    @Autowired
    private UserRepository repo;

    @GetMapping("")
    public String viewHomePage(){
        return "index";
    }

    @GetMapping("/info")
    public String showLogin(){

        return"info";
    }

    @GetMapping("/mainMenu")
    public String viewHomePage(Model model){
        return "index";
    }

    @GetMapping("/our_service")
    public String viewServicePage(){
        return "service";
    }
}

