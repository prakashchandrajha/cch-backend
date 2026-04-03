package com.ncdc.ica.cch.controller;

import com.ncdc.ica.cch.entity.Nomination;
import com.ncdc.ica.cch.entity.NominationFile;
import com.ncdc.ica.cch.service.NominationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nominations")
@RequiredArgsConstructor
@CrossOrigin
public class NominationController {

    private final NominationService nominationService;

    // ✅ POST
    @PostMapping(consumes = "multipart/form-data")
    public Long save(
            @RequestPart("form") Nomination nomination,
            @RequestPart(value = "lettersConsent", required = false) MultipartFile lettersConsent,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
            @RequestPart(value = "archivalMaterials", required = false) MultipartFile archivalMaterials,
            @RequestPart(value = "references", required = false) MultipartFile references
    ) throws Exception {

        return nominationService.saveNomination(
                nomination,
                lettersConsent,
                photos,
                archivalMaterials,
                references
        );
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(nominationService.getAll());
    }

    // ✅ DOWNLOAD FILE
    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {

        NominationFile file = nominationService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(file.getData());
    }
}