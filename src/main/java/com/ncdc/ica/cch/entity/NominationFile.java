package com.ncdc.ica.cch.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nomination_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NominationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileType; // lettersConsent, photo, archivalMaterials, references
    private String fileName;
    private String contentType;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "file_data", nullable = true)
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "nomination_id")
    private Nomination nomination;
}
