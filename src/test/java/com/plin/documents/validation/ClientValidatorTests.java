package com.plin.documents.validation;

import com.plin.documents.dto.ClientCreateDTO;
import com.plin.documents.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ClientValidatorTests {

    @InjectMocks
    private ClientValidator validator;

    private ClientCreateDTO clientCreateDto;

    @BeforeEach
    void setUp(){
        clientCreateDto = new ClientCreateDTO("João", "joao@email.com");
    }

    @Test
    public void validateShouldDoNothingWhenValidData(){
        Assertions.assertDoesNotThrow(() -> validator.validate(clientCreateDto));
    }

    @Test
    public void validateShouldThrowValidationExceptionWhenNameIsNull(){
        clientCreateDto.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(clientCreateDto));
    }

    @Test
    public void validateShouldThrowValidationExceptionWhenNameIsBlank(){
        clientCreateDto.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(clientCreateDto));
    }

    @Test
    public void validateShouldThrowValidationExceptionWhenEmailIsNull(){
        clientCreateDto.setEmail(null);
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(clientCreateDto));
    }

    @Test
    public void validateShouldThrowValidationExceptionWhenEmailIsBlank(){
        clientCreateDto.setEmail(" ");
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(clientCreateDto));
    }
}
