package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class ReportController {

    @GetMapping("/report")
    public String showInventory(Model model){
        return "report/report";
    }
}
