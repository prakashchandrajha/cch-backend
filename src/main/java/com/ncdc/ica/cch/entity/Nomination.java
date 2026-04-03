package com.ncdc.ica.cch.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "nominations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Section A
    private String organizationName;
    private String country;
    private String contactPerson;
    private String position;
    private String email;
    private String telephone;

    // Section B
    private String officialName;
    private String localName;
    private String otherNames;

    // Section C
    private Boolean tangible;
    private Boolean intangible;

    // Section D–K
    @Column(columnDefinition = "TEXT") private String communities;
    @Column(columnDefinition = "TEXT") private String geographicScope;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(columnDefinition = "TEXT") private String holders;
    @Column(columnDefinition = "TEXT") private String knowledgeTransmission;
    @Column(columnDefinition = "TEXT") private String socialFunctions;
    @Column(columnDefinition = "TEXT") private String humanRights;
    @Column(columnDefinition = "TEXT") private String safeguardingPast;
    @Column(columnDefinition = "TEXT") private String safeguardingFuture;
    @Column(columnDefinition = "TEXT") private String safeguardingCommunity;

    // Criteria
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

    @Column(columnDefinition = "TEXT")
    private String criteriaExplanation;

    // Section M–N
    @Column(columnDefinition = "TEXT")
    private String consentParticipation;

    @Column(columnDefinition = "TEXT")
    private String documentationInventories;

    // Section P
    private String declarantName;
    private String declarantDesignation;
    private String declarantOrganization;
    private LocalDate declarationDate;
    private String icaMember;
    private String icaAffiliated;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}