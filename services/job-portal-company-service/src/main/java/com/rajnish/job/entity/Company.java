package com.rajnish.job.entity;

import com.rajnish.job.enums.CompanySize;
import com.rajnish.job.enums.CompanyStatus;
import com.rajnish.job.enums.CompanyType;
import com.rajnish.job.enums.IndustryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true)
    private String slug;

    private String description;

    private String tagline;
    private String logoUrl;
    private String coverImageUrl;
    private String website;
    private Integer foundedYear;
    private String phone;
    private String email;

    @Enumerated(EnumType.STRING)
    private CompanySize companySize;

    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Enumerated(EnumType.STRING)
    private IndustryType industryType;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    @Column(unique = true)
    private String registrationNumber;

    @Column(unique = true, nullable = false)
    private Long ownerId;

    @ElementCollection
    private List<SocialLink> socialLinks = new ArrayList<>();

    private Boolean active = true;
    private LocalDateTime verifiedAt;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
