package com.rajnish.job.entity.embaddable;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class JobLocation {

    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
}
