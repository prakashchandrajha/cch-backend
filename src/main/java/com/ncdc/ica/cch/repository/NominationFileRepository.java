package com.ncdc.ica.cch.repository;

import com.ncdc.ica.cch.entity.NominationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NominationFileRepository extends JpaRepository<NominationFile, Long> {

    List<NominationFile> findByNominationId(Long nominationId);

    Optional<NominationFile> findByIdAndNominationId(Long fileId, Long nominationId);
}
