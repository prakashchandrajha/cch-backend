package com.ncdc.ica.cch.service;

import com.ncdc.ica.cch.entity.Nomination;
import com.ncdc.ica.cch.entity.NominationFile;
import com.ncdc.ica.cch.repository.NominationFileRepository;
import com.ncdc.ica.cch.repository.NominationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NominationService {

    private final NominationRepository nominationRepository;
    private final NominationFileRepository fileRepository;

    public Long saveNomination(
            Nomination nomination,
            MultipartFile lettersConsent,
            List<MultipartFile> photos,
            MultipartFile archivalMaterials,
            MultipartFile references
    ) throws Exception {

        // Save main entity
        nomination = nominationRepository.save(nomination);

        // Save files
        saveFile(lettersConsent, "lettersConsent", nomination);

        if (photos != null && !photos.isEmpty()) {
            for (MultipartFile photo : photos) {
                saveFile(photo, "photo", nomination);
            }
        }

        saveFile(archivalMaterials, "archivalMaterials", nomination);
        saveFile(references, "references", nomination);

        return nomination.getId();
    }

    private void saveFile(MultipartFile file, String type, Nomination nomination) throws Exception {
        if (file != null && !file.isEmpty()) {

            NominationFile f = NominationFile.builder()
                    .fileType(type)
                    .fileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .data(file.getBytes())
                    .nomination(nomination)
                    .build();

            fileRepository.save(f);
        }
    }

    public List<Nomination> getAll() {
        return nominationRepository.findAll();
    }

    public NominationFile getFile(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with id: " + id));
    }
}