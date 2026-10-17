package com.rajnish.job.service;

import com.rajnish.job.client.GeminiClient;
import com.rajnish.job.payload.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeAiService {

    private final GeminiClient geminiClient;


    String SYSTEM_PROMPT = """
        You are a senior resume writer and career coach with 15+ years of experience in the Indian tech job market.
        You specialize in ATS-optimized resumes, career coaching, and professional branding.
        Always be specific and results-oriented. Never use generic phrases like "hard-working", "team player".
        When asked for JSON, respond ONLY with valid JSON — no explanation, no markdown fences.
        """;


    public AiTextResponse generateProfessionalSummary(ResumeSummaryRequest request){
        String experiences = request.getWorkExperiences() != null
                ? request.getWorkExperiences().stream()
                .map(e->e.getJobTitle() + "at"+e.getCompany() +
                        (e.getDescription() != null && !e.getDescription().isBlank() ?": "+e.getDescription():""))
                .collect(Collectors.joining(": ")) : "Not Provided";


        String skills = request.getSkills() != null
                ? String.join(", ", request.getSkills())
                : "Not provided";

        String educations = request.getEducations() != null
                ? request.getEducations().stream()
                .map(e -> e.getDegree()
                        + (e.getFiledOfStudy() != null
                        ? " in " + e.getFiledOfStudy()
                        : "")
                        + (e.getInstitutionName() != null
                        ? " from " + e.getInstitutionName()
                        : ""))
                .collect(Collectors.joining("; "))
                : "Not provided";


        String prompt = """
        Write a compelling professional summary for a resume.

        Candidate Profile:
        - Target Job Title: %s
        - Years of Experience: %d
        - Work Experience: %s
        - Key Skills: %s
        - Education: %s

        Write a 3-4 sentence professional summary that:
        1. Opens with seniority level and area of expertise
        2. Highlights 2-3 key achievements or strengths with impact
        3. Mentions specific technical skills relevant to the target role
        4. Ends with a value proposition or career goal

        Rules:
        - Write in first person (no "I" at the start)
        - Be specific and results-oriented
        - Keep it under 80 words
        - Make it ATS-friendly
        """.formatted(
                request.getTargetJobTitle() != null ? request.getTargetJobTitle() : "Software Developer",
                request.getYearsOfExperience() != null ? request.getYearsOfExperience() : 0,
                experiences, skills, educations
        );


        return AiTextResponse.builder()
                .content(geminiClient.generateText(SYSTEM_PROMPT, prompt))
                .build();

    }

    public WorkExperienceBulletResponse generateWorkExperienceBullet(WorkExperienceBulletRequest request){


        String prompt = """
        Transform this work experience into powerful, ATS-friendly resume bullet points.

        Role: %s at %s
        Raw Description: %s
        Achievements/Hints: %s

        Generate exactly 4-5 bullet points that:
        1. Start with strong action verbs (Developed, Led, Implemented, Architected, Optimized, Reduced, Increased, Delivered, Built, Designed.)
        2. Include quantifiable metrics where possible (percentages, numbers, time saved)
        3. Highlight business impact and technical achievements
        4. Are concise (under 20 words each)
        5. Are ATS-friendly with relevant keywords

        {
            "bullets": [
                "bullet point 1",
                "bullet point 2",
                "bullet point 3",
                "bullet point 4",
                "bullet point 5"
            ]
        }
        """.formatted(
                request.getJobTitle(),
                request.getCompany() != null ? request.getCompany() : "the company",
                request.getRawDescriptions(),
                request.getAchievementsHint() != null ? request.getAchievementsHint() : "None"
        );

        return geminiClient.generateJson(SYSTEM_PROMPT, prompt, WorkExperienceBulletResponse.class);



    }

    public CareerFeedbackResponse generateCareerFeedback(CareerFeedbackRequest request){

        String prompt = """
        Analyze this resume and deliver an honest, actionable career feedback report.

        Target Job Title (if provided): %s
        Resume Content:
        %s

        {
            "profileStrength": 65,
            "shortlistingIssues": [
                "Reason 1 why recruiters are skipping this profile",
                "Reason 2",
                "Reason 3"
            ],
            "improvements": [
                {
                    "area": "Skills | Summary | Experience | Education | Projects | General",
                    "issue": "What specific issue exists in this area",
                    "suggestion": "How to fix this issue with actionable advice"
                }
            ],
            "targetJobs": [
                {
                    "jobTitle": "Recommended Job Title",
                    "reason": "Why this role suits the current profile",
                    "matchPercentage": 85
                }
            ],
            "overallSummary": "2-3 sentences of honest, encouraging career advice"
        }

        Rules:
        - profileStrength: integer 0-100 reflecting overall job market readiness
        - shortlistingIssues: 3-5 candid reasons a recruiter would skip this resume
        - improvements: 4-6 items ordered by priority descending
        - targetJobs: 3-5 realistic job titles matching current skills and experience level
        - Be specific — mention actual skills, tools, or sections by name
        - Provide honest, actionable feedback based on the resume content
        - Do not invent skills, experience, or achievements not present in the resume
        - Return ONLY valid JSON, with no explanation or markdown fences
        """.formatted(
                request.getTargetJobTitle() != null ? request.getTargetJobTitle() : "Not Specified",
                request.getResumeContent()
        );

        return geminiClient.generateJson(SYSTEM_PROMPT, prompt, CareerFeedbackResponse.class);
    }

    public ResumeImprovementResponse generateResumeImprovement(ResumeImprovementRequest request){

        String prompt = """
        Analyze this resume and provide specific, actionable improvement suggestions.

        Target Job Title: %s

        Resume Content:
        %s

        {
            "overallScore": 72,
            "improvements": [
                {
                    "section": "Summary or Experience or Skills or Education or General",
                    "issue": "What is wrong or missing",
                    "suggestion": "Specific actionable improvement",
                    "priority": "High or medium or low"
                }
            ],
            "strengths": [
                "what is already good about this resume"
            ],
            "summary": "2-sentence overall assessment"
        }

        Provide 4-6 specific improvements. Score should be 0-100.
        """.formatted(
                request.getTargetJobTitle() != null ? request.getTargetJobTitle() : "Not Specified",
                request.getResumeContent()
        );

        return geminiClient.generateJson(SYSTEM_PROMPT, prompt, ResumeImprovementResponse.class);

    }



}
