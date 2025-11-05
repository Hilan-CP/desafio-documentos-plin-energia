package com.plin.documents.validation;

import com.plin.documents.dto.ClientCreateDTO;
import com.plin.documents.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ClientValidator {
    public void validate(ClientCreateDTO client){
        if(client.getName() == null || client.getName().isBlank()){
            throw new ValidationException("Nome do cliente não pode ser vazio");
        }
        if(client.getEmail() == null || client.getEmail().isBlank()){
            throw new ValidationException("Email do cliente não pode ser vazio");
        }
    }
}
