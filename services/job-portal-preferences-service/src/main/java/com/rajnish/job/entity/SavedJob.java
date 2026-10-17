package com.rajnish.job.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "saved_job")
@Entity
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long candidateId;

    @Column(nullable = false)
    private Long jobId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime savedAt;
}
