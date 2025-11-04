package com.plin.documents.dto;

import org.springframework.core.io.ByteArrayResource;

public class DocumentContentDTO {
    private String title;
    private ByteArrayResource content;

    public DocumentContentDTO() {
    }

    public DocumentContentDTO(String title, ByteArrayResource content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ByteArrayResource getContent() {
        return content;
    }

    public void setContent(ByteArrayResource content) {
        this.content = content;
    }
}
