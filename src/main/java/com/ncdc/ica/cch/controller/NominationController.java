package com.ncdc.ica.cch.controller;

import com.ncdc.ica.cch.dto.NominationDetail;
import com.ncdc.ica.cch.dto.NominationRequest;
import com.ncdc.ica.cch.dto.NominationSummary;
import com.ncdc.ica.cch.entity.NominationFile;
import com.ncdc.ica.cch.service.NominationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/nominations")
@RequiredArgsConstructor
public class NominationController {

    private final NominationService nominationService;

    /**
     * API 1: POST — Submit nomination form with optional file attachments.
     * Uses multipart/form-data to handle file uploads alongside form fields.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NominationDetail> submitNomination(
            @ModelAttribute NominationRequest request,
            @RequestParam(value = "lettersConsent", required = false) MultipartFile lettersConsent,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @RequestParam(value = "archivalMaterials", required = false) MultipartFile archivalMaterials,
            @RequestParam(value = "references", required = false) MultipartFile references
    ) throws IOException {
        NominationDetail detail = nominationService.submitNomination(request, lettersConsent, photos, archivalMaterials, references);
        return ResponseEntity.ok(detail);
    }

    /**
     * API 2: GET — List all submitted nominations (summary view for table display).
     */
    @GetMapping
    public ResponseEntity<List<NominationSummary>> listNominations() {
        return ResponseEntity.ok(nominationService.getAllNominations());
    }

    /**
     * API 2b: GET — Get full detail of a single nomination by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NominationDetail> getNominationDetail(@PathVariable Long id) {
        return ResponseEntity.ok(nominationService.getNominationDetail(id));
    }

    /**
     * API 3: GET — Download an attached file from a nomination.
     */
    @GetMapping("/{nominationId}/files/{fileId}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long nominationId,
            @PathVariable Long fileId) {
        NominationFile file = nominationService.getFileForDownload(nominationId, fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getFileSize()))
                .body(file.getFileData());
    }
}
