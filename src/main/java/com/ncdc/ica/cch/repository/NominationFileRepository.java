package com.ncdc.ica.cch.repository;


import com.ncdc.ica.cch.entity.NominationFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NominationFileRepository extends JpaRepository<NominationFile, Long> {
    List<NominationFile> findByNominationId(Long nominationId);
}
