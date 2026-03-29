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

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@CrossOrigin("*")
public class NominationController {

    private final NominationService service;
    private final PdfService pdfService;

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<NominationForm> create(@RequestBody NominationForm form) {
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