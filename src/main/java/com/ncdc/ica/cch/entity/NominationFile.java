package com.ncdc.ica.cch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "nomination_files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NominationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_key", nullable = false)
    private String fileKey;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "file_data", nullable = false, columnDefinition = "bytea")
    private byte[] fileData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nomination_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Nomination nomination;
}
