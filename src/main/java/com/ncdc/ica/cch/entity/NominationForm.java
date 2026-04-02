package com.ncdc.ica.cch.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "nomination_forms")
@Data
public class NominationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Section A: Nominating ICA Member Organization
    private String organizationName;
    private String country;
    private String contactPerson;
    private String position;
    private String email;
    private String telephone;

    // ICA membership status
    private Boolean icaMember;
    @JsonProperty("icaAffiliated")
    private Boolean affiliatedToIcaMember;

    // Section B: Nomination Details
    @Column(length = 300)
    private String officialName;
    private String localName;
    private String otherNames;

    // Section C: Category of Heritage
    private Boolean tangible = false;
    private Boolean intangible = false;

    // Section D: Communities and Groups Concerned
    @Column(columnDefinition = "TEXT")
    private String communities;

    // Section E: Geographic Scope and Location
    @Column(columnDefinition = "TEXT")
    private String geographicScope;

    // Section F: Description of the Heritage Element
    @Column(columnDefinition = "TEXT")
    private String description;

    // Section G: Holders and Practitioners
    @Column(columnDefinition = "TEXT")
    private String holders;

    // Section H: Knowledge Transmission
    @Column(columnDefinition = "TEXT")
    private String knowledgeTransmission;

    // Section I: Social and Cultural Functions
    @Column(columnDefinition = "TEXT")
    private String socialFunctions;

    // Section J: Human Rights and Sustainability
    @Column(columnDefinition = "TEXT")
    private String humanRights;

    // Section K: Safeguarding Measures
    @Column(columnDefinition = "TEXT")
    private String safeguardingPast;

    @Column(columnDefinition = "TEXT")
    private String safeguardingFuture;

    @Column(columnDefinition = "TEXT")
    private String safeguardingCommunity;

    // Section L: Contribution to Visibility and Dialogue
    @Column(columnDefinition = "TEXT")
    private String visibilityCooperative;

    @Column(columnDefinition = "TEXT")
    private String dialogueBetweenCommunities;

    @Column(columnDefinition = "TEXT")
    private String respectDiversity;

    // Section M: Alignment with ICA CCH Standards (13 Point Criteria)
    private Boolean criterion1 = false;
    private Boolean criterion2 = false;
    private Boolean criterion3 = false;
    private Boolean criterion4 = false;
    private Boolean criterion5 = false;
    private Boolean criterion6 = false;
    private Boolean criterion7 = false;
    private Boolean criterion8 = false;
    private Boolean criterion9 = false;
    private Boolean criterion10 = false;
    private Boolean criterion11 = false;
    private Boolean criterion12 = false;
    private Boolean criterion13 = false;

    @Column(columnDefinition = "TEXT")
    private String criteriaExplanation;

    // Section N: Consent and Participation
    @Column(columnDefinition = "TEXT")
    private String consentParticipation;

    // Section O: Documentation and Inventories
    @Column(columnDefinition = "TEXT")
    private String documentationInventories;

    // Section P: Supporting Materials
    private String lettersConsentFileName;
    private String lettersConsentFileType;
    @Lob @Basic(fetch = FetchType.LAZY) @JsonIgnore
    private byte[] lettersConsentFileData;

    private String photosFileName;
    private String photosFileType;
    @Lob @Basic(fetch = FetchType.LAZY) @JsonIgnore
    private byte[] photosFileData;

    private Boolean video = false;

    private String archivalMaterialsFileName;
    private String archivalMaterialsFileType;
    @Lob @Basic(fetch = FetchType.LAZY) @JsonIgnore
    private byte[] archivalMaterialsFileData;

    private String referencesFileName;
    private String referencesFileType;
    @Lob @Basic(fetch = FetchType.LAZY) @JsonIgnore
    private byte[] referencesFileData;

    // Section Q: Declaration and Signature
    private String declarantName;
    private String declarantDesignation;
    private String declarantOrganization;
    private LocalDate declarationDate;

    // Metadata
    private LocalDate submissionDate;
    private LocalDateTime createdAt = LocalDateTime.now();
}