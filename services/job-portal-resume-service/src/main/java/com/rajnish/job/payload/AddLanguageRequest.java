package com.rajnish.job.payload;

import com.rajnish.job.enums.LanguageProficiency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddLanguageRequest {

    @NotBlank(message = "language name is required")
    private String languageName;

    @NotNull(message = "proficiency level is required")
    private LanguageProficiency proficiency;

    private Integer displayOrder;
}
