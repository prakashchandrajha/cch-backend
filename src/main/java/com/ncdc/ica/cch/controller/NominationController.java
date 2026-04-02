package com.ncdc.ica.cch.controller;

import com.ncdc.ica.cch.dto.FormSummaryDTO;
import com.ncdc.ica.cch.dto.FormInfoDTO;
import com.ncdc.ica.cch.entity.NominationForm;
import com.ncdc.ica.cch.service.NominationService;
import com.ncdc.ica.cch.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NominationController {

    private final NominationService service;
    private final PdfService pdfService;

    // ✅ CREATE
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<NominationForm> create(
        @RequestPart(value = "form", required = false) NominationForm form,
        @RequestBody(required = false) NominationForm bodyForm,
        @RequestPart(value = "lettersConsent", required = false) MultipartFile lettersConsent,
        @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
        @RequestPart(value = "archivalMaterials", required = false) MultipartFile archivalMaterials,
        @RequestPart(value = "references", required = false) MultipartFile references) {

        NominationForm payload = form != null ? form : bodyForm;
        if (payload == null) {
            throw new IllegalArgumentException("Missing nomination form payload");
        }

        if (payload.getIcaMember() == null || payload.getAffiliatedToIcaMember() == null) {
            throw new IllegalArgumentException("ICA Member and Affiliation are required.");
        }

        if (Boolean.TRUE.equals(payload.getTangible()) == Boolean.TRUE.equals(payload.getIntangible())) {
            throw new IllegalArgumentException("Select exactly one heritage category (tangible or intangible).");
        }

        try {
            if (lettersConsent != null) {
                payload.setLettersConsentFileName(lettersConsent.getOriginalFilename());
                payload.setLettersConsentFileType(lettersConsent.getContentType());
                payload.setLettersConsentFileData(lettersConsent.getBytes());
            }
            if (photos != null) {
                if (photos.size() > 5) {
                    throw new IllegalArgumentException("Maximum 5 photos are allowed.");
                }
                if (photos.size() == 1) {
                    MultipartFile photo = photos.get(0);
                    payload.setPhotosFileName(photo.getOriginalFilename());
                    payload.setPhotosFileType(photo.getContentType());
                    payload.setPhotosFileData(photo.getBytes());
                } else {
                    byte[] zipped = zipFiles(photos);
                    payload.setPhotosFileName("photos.zip");
                    payload.setPhotosFileType("application/zip");
                    payload.setPhotosFileData(zipped);
                }
            }
            if (archivalMaterials != null) {
                payload.setArchivalMaterialsFileName(archivalMaterials.getOriginalFilename());
                payload.setArchivalMaterialsFileType(archivalMaterials.getContentType());
                payload.setArchivalMaterialsFileData(archivalMaterials.getBytes());
            }
            if (references != null) {
                payload.setReferencesFileName(references.getOriginalFilename());
                payload.setReferencesFileType(references.getContentType());
                payload.setReferencesFileData(references.getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read uploaded files", e);
        }

        return ResponseEntity.ok(service.create(payload));
    }

    private byte[] zipFiles(List<MultipartFile> files) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "photo";
                ZipEntry entry = new ZipEntry(filename);
                zos.putNextEntry(entry);
                zos.write(file.getBytes());
                zos.closeEntry();
            }
            zos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to zip photos", e);
        }
    }

    // ✅ GET ALL (FULL - for debugging/admin)
    @GetMapping
    public List<NominationForm> getAll() {
        return service.getAll();
    }

    // ✅ SUMMARY (MAIN API FOR TABLE)
    @GetMapping("/summary")
    public List<FormSummaryDTO> getSummary() {
        return service.getAll().stream().map(form -> {
            FormSummaryDTO dto = new FormSummaryDTO();
            dto.setId(form.getId());
            dto.setOrganizationName(form.getOrganizationName());
            dto.setCountry(form.getCountry());
            dto.setOfficialName(form.getOfficialName());
            dto.setContactPerson(form.getContactPerson());
            dto.setSubmissionDate(form.getSubmissionDate());
            dto.setIcaMember(form.getIcaMember());
            dto.setIcaAffiliated(form.getAffiliatedToIcaMember());
            dto.setHasLettersConsent(form.getLettersConsentFileData() != null);
            dto.setHasPhotos(form.getPhotosFileData() != null);
            dto.setHasArchivalMaterials(form.getArchivalMaterialsFileData() != null);
            dto.setHasReferences(form.getReferencesFileData() != null);
            return dto;
        }).toList();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {

        NominationForm form = service.getById(id);
        byte[] pdf = pdfService.generatePdf(form);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=nomination_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ✅ DOWNLOAD INDIVIDUAL FILES
    @GetMapping("/{id}/file/{fileType}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id, @PathVariable String fileType) {
        NominationForm form = service.getById(id);
        byte[] data = null;
        String fileName = "download";
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        if ("lettersConsent".equals(fileType) && form.getLettersConsentFileData() != null) {
            data = form.getLettersConsentFileData();
            fileName = form.getLettersConsentFileName();
            contentType = form.getLettersConsentFileType();
        } else if ("photos".equals(fileType) && form.getPhotosFileData() != null) {
            data = form.getPhotosFileData();
            fileName = form.getPhotosFileName();
            contentType = form.getPhotosFileType();
        } else if ("archivalMaterials".equals(fileType) && form.getArchivalMaterialsFileData() != null) {
            data = form.getArchivalMaterialsFileData();
            fileName = form.getArchivalMaterialsFileName();
            contentType = form.getArchivalMaterialsFileType();
        } else if ("references".equals(fileType) && form.getReferencesFileData() != null) {
            data = form.getReferencesFileData();
            fileName = form.getReferencesFileName();
            contentType = form.getReferencesFileType();
        }

        if (data == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(data);
    }

    // ✅ GET ONE
    @GetMapping("/{id}")
    public NominationForm getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/{id}/info")
    public FormInfoDTO getFormInfo(@PathVariable Long id) {
        NominationForm form = service.getById(id);
        FormInfoDTO dto = new FormInfoDTO();
        dto.setId(form.getId());
        dto.setOrganizationName(form.getOrganizationName());
        dto.setCountry(form.getCountry());
        dto.setContactPerson(form.getContactPerson());
        dto.setPosition(form.getPosition());
        dto.setEmail(form.getEmail());
        dto.setTelephone(form.getTelephone());
        dto.setIcaMember(form.getIcaMember());
        dto.setIcaAffiliated(form.getAffiliatedToIcaMember());
        dto.setOfficialName(form.getOfficialName());
        dto.setLocalName(form.getLocalName());
        dto.setOtherNames(form.getOtherNames());
        dto.setTangible(form.getTangible());
        dto.setIntangible(form.getIntangible());
        dto.setCommunities(form.getCommunities());
        dto.setGeographicScope(form.getGeographicScope());
        dto.setDescription(form.getDescription());
        dto.setHolders(form.getHolders());
        dto.setKnowledgeTransmission(form.getKnowledgeTransmission());
        dto.setSocialFunctions(form.getSocialFunctions());
        dto.setHumanRights(form.getHumanRights());
        dto.setSafeguardingPast(form.getSafeguardingPast());
        dto.setSafeguardingFuture(form.getSafeguardingFuture());
        dto.setSafeguardingCommunity(form.getSafeguardingCommunity());
        dto.setVisibilityCooperative(form.getVisibilityCooperative());
        dto.setDialogueBetweenCommunities(form.getDialogueBetweenCommunities());
        dto.setRespectDiversity(form.getRespectDiversity());
        dto.setCriterion1(form.getCriterion1());
        dto.setCriterion2(form.getCriterion2());
        dto.setCriterion3(form.getCriterion3());
        dto.setCriterion4(form.getCriterion4());
        dto.setCriterion5(form.getCriterion5());
        dto.setCriterion6(form.getCriterion6());
        dto.setCriterion7(form.getCriterion7());
        dto.setCriterion8(form.getCriterion8());
        dto.setCriterion9(form.getCriterion9());
        dto.setCriterion10(form.getCriterion10());
        dto.setCriterion11(form.getCriterion11());
        dto.setCriterion12(form.getCriterion12());
        dto.setCriterion13(form.getCriterion13());
        dto.setCriteriaExplanation(form.getCriteriaExplanation());
        dto.setConsentParticipation(form.getConsentParticipation());
        dto.setDocumentationInventories(form.getDocumentationInventories());
        dto.setVideo(form.getVideo());
        dto.setDeclarantName(form.getDeclarantName());
        dto.setDeclarantDesignation(form.getDeclarantDesignation());
        dto.setDeclarantOrganization(form.getDeclarantOrganization());
        dto.setDeclarationDate(form.getDeclarationDate());
        dto.setSubmissionDate(form.getSubmissionDate());

        dto.setHasLettersConsent(form.getLettersConsentFileData() != null);
        dto.setHasPhotos(form.getPhotosFileData() != null);
        dto.setHasArchivalMaterials(form.getArchivalMaterialsFileData() != null);
        dto.setHasReferences(form.getReferencesFileData() != null);

        dto.setLettersConsentUrl(String.format("/cch/api/forms/%d/file/lettersConsent", id));
        dto.setPhotosUrl(String.format("/cch/api/forms/%d/file/photos", id));
        dto.setArchivalMaterialsUrl(String.format("/cch/api/forms/%d/file/archivalMaterials", id));
        dto.setReferencesUrl(String.format("/cch/api/forms/%d/file/references", id));
        dto.setPdfUrl(String.format("/cch/api/forms/%d/pdf", id));

        return dto;
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}