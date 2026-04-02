package com.ncdc.ica.cch.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FormInfoDTO {
    private Long id;
    private String organizationName;
    private String country;
    private String contactPerson;
    private String position;
    private String email;
    private String telephone;
    private Boolean icaMember;
    private Boolean icaAffiliated;
    private String officialName;
    private String localName;
    private String otherNames;
    private Boolean tangible;
    private Boolean intangible;
    private String communities;
    private String geographicScope;
    private String description;
    private String holders;
    private String knowledgeTransmission;
    private String socialFunctions;
    private String humanRights;
    private String safeguardingPast;
    private String safeguardingFuture;
    private String safeguardingCommunity;
    private String visibilityCooperative;
    private String dialogueBetweenCommunities;
    private String respectDiversity;
    private Boolean criterion1;
    private Boolean criterion2;
    private Boolean criterion3;
    private Boolean criterion4;
    private Boolean criterion5;
    private Boolean criterion6;
    private Boolean criterion7;
    private Boolean criterion8;
    private Boolean criterion9;
    private Boolean criterion10;
    private Boolean criterion11;
    private Boolean criterion12;
    private Boolean criterion13;
    private String criteriaExplanation;
    private String consentParticipation;
    private String documentationInventories;
    private Boolean video;
    private String declarantName;
    private String declarantDesignation;
    private String declarantOrganization;
    private LocalDate declarationDate;
    private LocalDate submissionDate;

    private boolean hasLettersConsent;
    private boolean hasPhotos;
    private boolean hasArchivalMaterials;
    private boolean hasReferences;
    private String lettersConsentUrl;
    private String photosUrl;
    private String archivalMaterialsUrl;
    private String referencesUrl;
    private String pdfUrl;
}
