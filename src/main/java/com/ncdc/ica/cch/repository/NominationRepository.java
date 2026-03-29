package com.ncdc.ica.cch.repository;

import com.ncdc.ica.cch.entity.NominationForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NominationRepository extends JpaRepository<NominationForm, Long> {
}
