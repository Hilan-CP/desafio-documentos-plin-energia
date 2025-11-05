package com.plin.documents.mapper;

import com.plin.documents.dto.ClientCreateDTO;
import com.plin.documents.dto.ClientDTO;
import com.plin.documents.dto.ClientWithDocumentsDTO;
import com.plin.documents.dto.DocumentDTO;
import com.plin.documents.entity.Client;

import java.util.List;

public class ClientMapper {
    public static Client toEntity(ClientCreateDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    public static ClientDTO toClientDto(Client entity){
        return new ClientDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCreationDate(),
                null);
    }

    public static ClientWithDocumentsDTO toClientWithDocumentsDto(Client entity, List<DocumentDTO> documentDtoList){
        ClientWithDocumentsDTO dto = new ClientWithDocumentsDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCreationDate()
        );
        dto.getDocuments().addAll(documentDtoList);
        return dto;
    }
}
