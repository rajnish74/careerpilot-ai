package com.rajnish.job.controller;

import com.rajnish.job.enums.UserRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String home() {
        return "Hello World---"+ UserRole.ROLE_JOB_SEEKER;
    }
}
