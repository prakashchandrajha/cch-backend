package com.ncdc.ica.cch.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NominationSummary {

    private Long id;
    private String officialName;
    private String organizationName;
    private String country;
    private String heritageCategory;
    private String contactPerson;
    private String email;
    private LocalDateTime submittedAt;
}
