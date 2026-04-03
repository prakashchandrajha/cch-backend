package com.ncdc.ica.cch.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NominationDetail {

    private Long id;
    private LocalDateTime submittedAt;

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
    private Boolean tangible;
    private Boolean intangible;

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

    // ── Attached Files ──
    private List<FileInfo> files;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        private Long fileId;
        private String fileKey;
        private String fileName;
        private String contentType;
        private Long fileSize;
    }
}
