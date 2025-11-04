package com.plin.documents.dto;

import java.time.LocalDate;

public class ClientDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDate creationDate;
    private Long documentAmount;

    public ClientDTO() {
    }

    public ClientDTO(Long id, String name, String email, LocalDate creationDate, Long documentAmount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.creationDate = creationDate;
        this.documentAmount = documentAmount;
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

    public Long getDocumentAmount() {
        return documentAmount;
    }

    public void setDocumentAmount(Long documentAmount) {
        this.documentAmount = documentAmount;
    }
}
