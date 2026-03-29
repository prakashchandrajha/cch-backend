package com.ncdc.ica.cch.service;

import com.ncdc.ica.cch.entity.NominationForm;
import com.ncdc.ica.cch.repository.NominationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NominationService {

    private final NominationRepository repo;

    public NominationForm create(NominationForm form) {
        form.setSubmissionDate(LocalDate.now()); // backend controls date
        return repo.save(form);
    }

    public List<NominationForm> getAll() {
        return repo.findAll();
    }

    public NominationForm getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Form not found"));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}