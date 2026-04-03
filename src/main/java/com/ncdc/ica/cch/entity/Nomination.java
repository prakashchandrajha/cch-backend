package com.ncdc.ica.cch.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nominations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Nomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Section A: Nominating ICA Member Organization ──
    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "country")
    private String country;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "position")
    private String position;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

    // ── Section B: Nomination Details ──
    @Column(name = "official_name", columnDefinition = "TEXT")
    private String officialName;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "other_names")
    private String otherNames;

    // ── Section C: Category of Heritage ──
    @Column(name = "heritage_category")
    private String heritageCategory;

    @Column(name = "tangible")
    private Boolean tangible;

    @Column(name = "intangible")
    private Boolean intangible;

    // ── Section D: Communities and Groups Concerned ──
    @Column(name = "communities", columnDefinition = "TEXT")
    private String communities;

    // ── Section E: Geographic Scope and Location ──
    @Column(name = "geographic_scope", columnDefinition = "TEXT")
    private String geographicScope;

    // ── Section F: Description of the Heritage Element ──
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // ── Section G: Legacy And Knowledge Transmission ──
    @Column(name = "knowledge_transmission", columnDefinition = "TEXT")
    private String knowledgeTransmission;

    // ── Section H: Social and Cultural Functions ──
    @Column(name = "social_functions", columnDefinition = "TEXT")
    private String socialFunctions;

    // ── Section H (dead field from form, keeping for completeness) ──
    @Column(name = "holders")
    private String holders;

    // ── Section I: Human Rights and Sustainability ──
    @Column(name = "human_rights", columnDefinition = "TEXT")
    private String humanRights;

    // ── Section J: Safeguarding Measures ──
    @Column(name = "safeguarding_past", columnDefinition = "TEXT")
    private String safeguardingPast;

    @Column(name = "safeguarding_future", columnDefinition = "TEXT")
    private String safeguardingFuture;

    @Column(name = "safeguarding_community", columnDefinition = "TEXT")
    private String safeguardingCommunity;

    // ── Section K: CCH Criteria Alignment ──
    @Column(name = "criterion1")
    private Boolean criterion1;

    @Column(name = "criterion2")
    private Boolean criterion2;

    @Column(name = "criterion3")
    private Boolean criterion3;

    @Column(name = "criterion4")
    private Boolean criterion4;

    @Column(name = "criterion5")
    private Boolean criterion5;

    @Column(name = "criterion6")
    private Boolean criterion6;

    @Column(name = "criterion7")
    private Boolean criterion7;

    @Column(name = "criterion8")
    private Boolean criterion8;

    @Column(name = "criterion9")
    private Boolean criterion9;

    @Column(name = "criterion10")
    private Boolean criterion10;

    @Column(name = "criterion11")
    private Boolean criterion11;

    @Column(name = "criterion12")
    private Boolean criterion12;

    @Column(name = "criterion13")
    private Boolean criterion13;

    @Column(name = "criteria_explanation", columnDefinition = "TEXT")
    private String criteriaExplanation;

    // ── Section L: Documentation and Inventories ──
    @Column(name = "documentation_inventories", columnDefinition = "TEXT")
    private String documentationInventories;

    // ── Section M: Supporting Materials (dead field from form) ──
    @Column(name = "consent_participation")
    private String consentParticipation;

    // ── Section N: Declaration and Signature ──
    @Column(name = "declarant_name")
    private String declarantName;

    @Column(name = "declarant_designation")
    private String declarantDesignation;

    @Column(name = "declarant_organization")
    private String declarantOrganization;

    @Column(name = "declaration_date")
    private String declarationDate;

    @Column(name = "ica_member")
    private String icaMember;

    @Column(name = "ica_affiliated")
    private String icaAffiliated;

    // ── Metadata ──
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "nomination", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NominationFile> files = new ArrayList<>();
}
