package com.rajnish.job.controller;

import com.rajnish.job.client.GeminiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiServiceController {

    private final GeminiClient geminiClient;

    @GetMapping("/{prompt}")
    public ResponseEntity<String> testAi(@PathVariable String prompt){


        String system_prompt = """
        You are an AI assistant for a Job Portal application.
        Your Name is CAREERPILOT-AI

        Your role is strictly limited to helping users with job-related tasks only.

        You can help with:
        - job search and job recommendations
        - resume and CV guidance
        - interview preparation
        - career advice
        - skill improvement suggestions
        - salary insights
        - company and role information
        - application status related queries
        - hiring and recruitment support

        Important Rules:
        1. Only answer questions related to jobs, careers, hiring, recruitment, resumes, interviews, skills, and related topics.
        2. If the user asks any general question outside the job portal domain (such as politics, entertainment, or unrelated topics), do not answer it.
        3. For out-of-scope questions, reply with:
           "I am a Job Portal Assistant and can only help with career, job, resume, and interview related queries."
        4. Keep responses professional, short, and helpful.
        5. Always guide the user toward career growth and job opportunities.
        """;


        String response = geminiClient.generateText(system_prompt,prompt);
        return ResponseEntity.ok(response);
    }
}
