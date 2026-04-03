package com.ncdc.ica.cch.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NominationRequest {

    // ── Section A ──
    private String organizationName;
    private String country;
    private String contactPerson;
    private String position;
    private String email;
    private String telephone;

    // ── Section B ──
    private String officialName;
    private String localName;
    private String otherNames;

    // ── Section C ──
    private String heritageCategory;

    // ── Section D ──
    private String communities;

    // ── Section E ──
    private String geographicScope;

    // ── Section F ──
    private String description;

    // ── Section G ──
    private String knowledgeTransmission;

    // ── Section H ──
    private String socialFunctions;
    private String holders;

    // ── Section I ──
    private String humanRights;

    // ── Section J ──
    private String safeguardingPast;
    private String safeguardingFuture;
    private String safeguardingCommunity;

    // ── Section K ──
    private String criterion1;
    private String criterion2;
    private String criterion3;
    private String criterion4;
    private String criterion5;
    private String criterion6;
    private String criterion7;
    private String criterion8;
    private String criterion9;
    private String criterion10;
    private String criterion11;
    private String criterion12;
    private String criterion13;
    private String criteriaExplanation;

    // ── Section L ──
    private String documentationInventories;

    // ── Section M (dead field) ──
    private String consentParticipation;

    // ── Section N ──
    private String declarantName;
    private String declarantDesignation;
    private String declarantOrganization;
    private String declarationDate;
    private String icaMember;
    private String icaAffiliated;
}
