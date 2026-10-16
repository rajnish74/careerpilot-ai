package com.rajnish.job.entity;

import com.rajnish.job.dto.PersonalInfoResponse;
import com.rajnish.job.enums.ResumeTemplate;
import com.rajnish.job.enums.ResumeVisibility;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "resumes")
@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long candidateId;

    @Column(nullable = false)
    private String title;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResumeTemplate template = ResumeTemplate.PROFESSIONAL;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResumeVisibility visibility = ResumeVisibility.PUBLIC;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDefault = false;

    @Embedded
    private PersonalInfo personalInfo;

    private String summary;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Integer completionScore = 0;

    // Fix for display_order NOT NULL error
    @Builder.Default
    private Integer displayOrder = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}