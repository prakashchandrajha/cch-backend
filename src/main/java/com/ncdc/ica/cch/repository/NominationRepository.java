package com.ncdc.ica.cch.repository;

import com.ncdc.ica.cch.entity.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NominationRepository extends JpaRepository<Nomination, Long> {

    List<Nomination> findAllByOrderBySubmittedAtDesc();
}
