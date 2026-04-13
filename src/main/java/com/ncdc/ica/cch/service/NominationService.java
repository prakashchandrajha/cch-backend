package com.ncdc.ica.cch.service;

import com.ncdc.ica.cch.dto.NominationDetail;
import com.ncdc.ica.cch.dto.NominationRequest;
import com.ncdc.ica.cch.dto.NominationSummary;
import com.ncdc.ica.cch.entity.Nomination;
import com.ncdc.ica.cch.entity.NominationFile;
import com.ncdc.ica.cch.repository.NominationFileRepository;
import com.ncdc.ica.cch.repository.NominationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NominationService {

    private final NominationRepository nominationRepository;
    private final NominationFileRepository nominationFileRepository;

    private Boolean strBool(String val) {
        return "true".equalsIgnoreCase(val) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Transactional
    public NominationDetail submitNomination(NominationRequest request,
                                             MultipartFile lettersConsent,
                                             List<MultipartFile> photos,
                                             MultipartFile archivalMaterials,
                                             MultipartFile references) throws IOException {
        // heritageCategory comes from form as "tangible" or "intangible"
        String category = request.getHeritageCategory();
        Boolean tangible = "tangible".equalsIgnoreCase(category);
        Boolean intangible = "intangible".equalsIgnoreCase(category);

        Nomination nomination = Nomination.builder()
                .organizationName(request.getOrganizationName())
                .country(request.getCountry())
                .contactPerson(request.getContactPerson())
                .position(request.getPosition())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .officialName(request.getOfficialName())
                .localName(request.getLocalName())
                .otherNames(request.getOtherNames())
                .heritageCategory(category)
                .tangible(tangible)
                .intangible(intangible)
                .communities(request.getCommunities())
                .geographicScope(request.getGeographicScope())
                .description(request.getDescription())
                .knowledgeTransmission(request.getKnowledgeTransmission())
                .socialFunctions(request.getSocialFunctions())
                .holders(request.getHolders())
                .humanRights(request.getHumanRights())
                .safeguardingPast(request.getSafeguardingPast())
                .safeguardingFuture(request.getSafeguardingFuture())
                .safeguardingCommunity(request.getSafeguardingCommunity())
                .criterion1(strBool(request.getCriterion1()))
                .criterion2(strBool(request.getCriterion2()))
                .criterion3(strBool(request.getCriterion3()))
                .criterion4(strBool(request.getCriterion4()))
                .criterion5(strBool(request.getCriterion5()))
                .criterion6(strBool(request.getCriterion6()))
                .criterion7(strBool(request.getCriterion7()))
                .criterion8(strBool(request.getCriterion8()))
                .criterion9(strBool(request.getCriterion9()))
                .criterion10(strBool(request.getCriterion10()))
                .criterion11(strBool(request.getCriterion11()))
                .criterion12(strBool(request.getCriterion12()))
                .criterion13(strBool(request.getCriterion13()))
                .criteriaExplanation(request.getCriteriaExplanation())
                .documentationInventories(request.getDocumentationInventories())
                .consentParticipation(request.getConsentParticipation())
                .declarantName(request.getDeclarantName())
                .declarantDesignation(request.getDeclarantDesignation())
                .declarantOrganization(request.getDeclarantOrganization())
                .declarationDate(request.getDeclarationDate())
                .icaMember(request.getIcaMember())
                .icaAffiliated(request.getIcaAffiliated())
                .submittedAt(LocalDateTime.now())
                .files(new ArrayList<>())
                .build();

        nomination = nominationRepository.save(nomination);

        // Handle file uploads
        handleFileUpload(nomination, "lettersConsent", lettersConsent);
        handleFileUploadList(nomination, "photos", photos);
        handleFileUpload(nomination, "archivalMaterials", archivalMaterials);
        handleFileUpload(nomination, "references", references);

        // Re-fetch to get files
        nomination = nominationRepository.findById(nomination.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve saved nomination"));

        return toDetail(nomination);
    }

    private void handleFileUpload(Nomination nomination, String fileKey, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            NominationFile nomFile = NominationFile.builder()
                    .nomination(nomination)
                    .fileKey(fileKey)
                    .fileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .fileData(file.getBytes())
                    .build();
            nominationFileRepository.save(nomFile);
        }
    }

    private void handleFileUploadList(Nomination nomination, String fileKey, List<MultipartFile> files) throws IOException {
        if (files != null) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    handleFileUpload(nomination, fileKey, file);
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<NominationSummary> getAllNominations() {
        return nominationRepository.findAllByOrderBySubmittedAtDesc()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public NominationDetail getNominationDetail(Long id) {
        Nomination nomination = nominationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nomination not found with id: " + id));
        return toDetail(nomination);
    }

    @Transactional(readOnly = true)
    public NominationFile getFileForDownload(Long nominationId, Long fileId) {
        return nominationFileRepository.findByIdAndNominationId(fileId, nominationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
    }

    private NominationSummary toSummary(Nomination n) {
        return NominationSummary.builder()
                .id(n.getId())
                .officialName(n.getOfficialName())
                .organizationName(n.getOrganizationName())
                .country(n.getCountry())
                .heritageCategory(n.getHeritageCategory())
                .contactPerson(n.getContactPerson())
                .email(n.getEmail())
                .declarationDate(n.getDeclarationDate())
                .submittedAt(n.getSubmittedAt())
                .build();
    }

    private NominationDetail toDetail(Nomination n) {
        List<NominationDetail.FileInfo> fileInfos = n.getFiles().stream()
                .map(f -> NominationDetail.FileInfo.builder()
                        .fileId(f.getId())
                        .fileKey(f.getFileKey())
                        .fileName(f.getFileName())
                        .contentType(f.getContentType())
                        .fileSize(f.getFileSize())
                        .build())
                .toList();

        return NominationDetail.builder()
                .id(n.getId())
                .submittedAt(n.getSubmittedAt())
                .organizationName(n.getOrganizationName())
                .country(n.getCountry())
                .contactPerson(n.getContactPerson())
                .position(n.getPosition())
                .email(n.getEmail())
                .telephone(n.getTelephone())
                .officialName(n.getOfficialName())
                .localName(n.getLocalName())
                .otherNames(n.getOtherNames())
                .heritageCategory(n.getHeritageCategory())
                .tangible(n.getTangible())
                .intangible(n.getIntangible())
                .communities(n.getCommunities())
                .geographicScope(n.getGeographicScope())
                .description(n.getDescription())
                .knowledgeTransmission(n.getKnowledgeTransmission())
                .socialFunctions(n.getSocialFunctions())
                .holders(n.getHolders())
                .humanRights(n.getHumanRights())
                .safeguardingPast(n.getSafeguardingPast())
                .safeguardingFuture(n.getSafeguardingFuture())
                .safeguardingCommunity(n.getSafeguardingCommunity())
                .criterion1(n.getCriterion1())
                .criterion2(n.getCriterion2())
                .criterion3(n.getCriterion3())
                .criterion4(n.getCriterion4())
                .criterion5(n.getCriterion5())
                .criterion6(n.getCriterion6())
                .criterion7(n.getCriterion7())
                .criterion8(n.getCriterion8())
                .criterion9(n.getCriterion9())
                .criterion10(n.getCriterion10())
                .criterion11(n.getCriterion11())
                .criterion12(n.getCriterion12())
                .criterion13(n.getCriterion13())
                .criteriaExplanation(n.getCriteriaExplanation())
                .documentationInventories(n.getDocumentationInventories())
                .consentParticipation(n.getConsentParticipation())
                .declarantName(n.getDeclarantName())
                .declarantDesignation(n.getDeclarantDesignation())
                .declarantOrganization(n.getDeclarantOrganization())
                .declarationDate(n.getDeclarationDate())
                .icaMember(n.getIcaMember())
                .icaAffiliated(n.getIcaAffiliated())
                .files(fileInfos)
                .build();
    }
}
