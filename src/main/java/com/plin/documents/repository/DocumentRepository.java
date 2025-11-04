package com.plin.documents.repository;

import com.plin.documents.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByClientId(Long clientId);
}
