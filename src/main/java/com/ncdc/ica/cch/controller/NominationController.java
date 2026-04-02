package com.ncdc.ica.cch.controller;

import com.ncdc.ica.cch.dto.FormSummaryDTO;
import com.ncdc.ica.cch.entity.NominationForm;
import com.ncdc.ica.cch.service.NominationService;
import com.ncdc.ica.cch.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NominationController {

    private final NominationService service;
    private final PdfService pdfService;

    // ✅ CREATE
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NominationForm> create(
            @RequestPart("form") NominationForm form,
            @RequestPart(value = "lettersConsent", required = false) MultipartFile lettersConsent,
            @RequestPart(value = "photos", required = false) MultipartFile photos,
            @RequestPart(value = "archivalMaterials", required = false) MultipartFile archivalMaterials,
            @RequestPart(value = "references", required = false) MultipartFile references) {

        try {
            if (lettersConsent != null) {
                form.setLettersConsentFileName(lettersConsent.getOriginalFilename());
                form.setLettersConsentFileType(lettersConsent.getContentType());
                form.setLettersConsentFileData(lettersConsent.getBytes());
            }
            if (photos != null) {
                form.setPhotosFileName(photos.getOriginalFilename());
                form.setPhotosFileType(photos.getContentType());
                form.setPhotosFileData(photos.getBytes());
            }
            if (archivalMaterials != null) {
                form.setArchivalMaterialsFileName(archivalMaterials.getOriginalFilename());
                form.setArchivalMaterialsFileType(archivalMaterials.getContentType());
                form.setArchivalMaterialsFileData(archivalMaterials.getBytes());
            }
            if (references != null) {
                form.setReferencesFileName(references.getOriginalFilename());
                form.setReferencesFileType(references.getContentType());
                form.setReferencesFileData(references.getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read uploaded files", e);
        }

        return ResponseEntity.ok(service.create(form));
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

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}