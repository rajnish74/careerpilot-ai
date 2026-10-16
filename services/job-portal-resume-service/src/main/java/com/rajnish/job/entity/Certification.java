package com.rajnish.job.entity;

import com.rajnish.job.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "resume_certifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn( nullable = false)
    private Resume resume;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String issuingOrganization;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    @Column(length = 200)
    private String credentialId;

    @Column(length = 1000)
    private String credentialUrl;

    @Builder.Default
    private Boolean noExpiration = false;

    private Integer displayOrder;
}
