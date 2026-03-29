package com.ncdc.ica.cch.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FormSummaryDTO {
    private Long id;
    private String organizationName;
    private String country;
    private String officialName;
    private String contactPerson;
    private LocalDate submissionDate;
}