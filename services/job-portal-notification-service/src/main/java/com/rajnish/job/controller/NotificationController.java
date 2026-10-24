package com.rajnish.job.controller;

import com.rajnish.job.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailNotificationService emailNotificationService;

//    @GetMapping("/sent")
//    public String NotificationController() throws Exception {
//        emailNotificationService.sendStatusChangedEmail();
//        return "Successfully email sent";
//    }



}
