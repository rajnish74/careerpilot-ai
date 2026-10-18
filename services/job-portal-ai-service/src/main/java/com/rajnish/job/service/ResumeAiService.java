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

    Return JSON with exactly this structure:
    {
        "profileStrength": 65,
        "shortlistingIssues": [
            "Reason 1 why recruiters may skip this profile",
            "Reason 2",
            "Reason 3"
        ],
        "improvements": [
            {
                "area": "Skills",
                "issue": "Specific issue identified in the resume",
                "action": "A concrete, actionable step to fix the issue",
                "priority": "HIGH"
            },
            {
                "area": "Experience",
                "issue": "Specific issue identified in the resume",
                "action": "A concrete, actionable step to fix the issue",
                "priority": "MEDIUM"
            }
        ],
        "targetJob": [
            {
                "jobTitle": "Recommended Job Title",
                "reason": "Why this role suits the current profile",
                "skillMatch": "Explain the match using available resume evidence"
            }
        ],
        "overallSummary": "2-3 sentences of honest career advice"
    }

    Rules:
    - profileStrength must be an integer from 0 to 100.
    - shortlistingIssues must contain 3-5 specific issues.
    - improvements must contain 4-6 items, ordered from highest to lowest priority.
    - Every improvement MUST include all four non-null fields:
      area, issue, action, priority.
    - Use HIGH, MEDIUM, or LOW for priority.
    - The action field MUST contain a specific, practical recommendation.
    - Do not use "suggestion" as a JSON key. Use "action" only.
    - targetJob must contain 3-5 realistic job titles.
    - Each targetJob item MUST contain jobTitle, reason, and skillMatch.
    - Use only evidence supported by the resume. Do not invent skills,
      experience, achievements, or performance metrics.
    - Return ONLY valid JSON. No markdown fences or explanations.
    """.formatted(
                request.getTargetJobTitle() != null
                        ? request.getTargetJobTitle()
                        : "Not Specified",
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
