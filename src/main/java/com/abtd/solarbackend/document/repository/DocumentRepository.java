package com.abtd.solarbackend.document.repository;

import com.abtd.solarbackend.document.entity.Document;
import com.abtd.solarbackend.document.enums.ModuleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByModuleTypeAndReferenceId(
            ModuleType moduleType,
            Long referenceId
    );

}