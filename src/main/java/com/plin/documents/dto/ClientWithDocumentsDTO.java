package com.plin.documents.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientWithDocumentsDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDate creationDate;
    private List<DocumentDTO> documents = new ArrayList<>();

    public ClientWithDocumentsDTO() {
    }

    public ClientWithDocumentsDTO(Long id, String name, String email, LocalDate creationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public List<DocumentDTO> getDocuments() {
        return documents;
    }
}
