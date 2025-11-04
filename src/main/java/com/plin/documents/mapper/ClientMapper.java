package com.plin.documents.mapper;

import com.plin.documents.dto.ClientCreateDTO;
import com.plin.documents.dto.ClientDTO;
import com.plin.documents.entity.Client;

public class ClientMapper {
    public static Client toEntity(ClientCreateDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    public static ClientDTO toDto(Client entity){
        return new ClientDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCreationDate(),
                entity.getDocuments().size());
    }
}
