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

    private Boolean icaMember;
    private Boolean icaAffiliated;

    // File availability
    private boolean hasLettersConsent;
    private boolean hasPhotos;
    private boolean hasArchivalMaterials;
    private boolean hasReferences;
}