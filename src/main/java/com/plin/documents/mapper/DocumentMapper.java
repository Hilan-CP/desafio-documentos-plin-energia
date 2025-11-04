package com.plin.documents.mapper;

import com.plin.documents.dto.DocumentDTO;
import com.plin.documents.entity.Document;

public class DocumentMapper {

    public static DocumentDTO toDto(Document entity){
        return new DocumentDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getCreationDate(),
                entity.getClient().getId()
        );
    }
}
