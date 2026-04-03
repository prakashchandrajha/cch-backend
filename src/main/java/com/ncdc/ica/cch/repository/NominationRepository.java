package com.ncdc.ica.cch.repository;

import com.ncdc.ica.cch.entity.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NominationRepository extends JpaRepository<Nomination, Long> {
}