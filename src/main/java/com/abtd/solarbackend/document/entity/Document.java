package com.abtd.solarbackend.document.entity;

import com.abtd.solarbackend.document.enums.DocumentType;
import com.abtd.solarbackend.document.enums.ModuleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModuleType moduleType;

    @Column(nullable = false)
    private Long referenceId;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

    private String contentType;

    private Long fileSize;

    private String filePath;

    private String uploadedBy;

    private LocalDateTime uploadedAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    @Column(length = 500)
    private String description;

    private Boolean verified;

    private LocalDate verifiedAt;

    private String verifiedBy;

    @PrePersist
    public void prePersist() {
        uploadedAt = LocalDateTime.now();
    }
}