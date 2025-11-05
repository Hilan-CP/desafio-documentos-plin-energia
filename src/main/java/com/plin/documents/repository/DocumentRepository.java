package com.plin.documents.repository;

import com.plin.documents.dto.DocumentDTO;
import com.plin.documents.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("""
            SELECT NEW com.plin.documents.dto.DocumentDTO(
                document.id,
                document.title,
                document.creationDate)
            FROM Document document
            WHERE document.client.id = :clientId
            """)
    List<DocumentDTO> findAllByClientId(Long clientId);
}
