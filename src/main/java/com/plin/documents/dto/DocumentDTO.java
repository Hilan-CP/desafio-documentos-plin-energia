package com.plin.documents.dto;

import java.time.LocalDate;

public class DocumentDTO {
    private Long id;
    private String title;
    private LocalDate creationDate;

    public DocumentDTO() {
    }

    public DocumentDTO(Long id, String title, LocalDate creationDate) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
