package com.epam.esm.config;

import com.epam.esm.DTO.response.BaseExceptionDto;
import com.epam.esm.exception.NoDataFoundException;
import com.epam.esm.exception.UnknownDataBaseException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.exception.tag.TagAlreadyExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> classNotFoundExceptionHandler(Exception e){
        return ResponseEntity.ok(
                new BaseExceptionDto(500, e.getLocalizedMessage(), 10500)
        );
    }

    @ExceptionHandler(TagAlreadyExistException.class)
    public ResponseEntity<?> tagAlreadyExistExceptionHandler(TagAlreadyExistException e){
        return ResponseEntity.badRequest().body(
                new BaseExceptionDto(400, e.getMessage(), 10400)
        );
    }

    @ExceptionHandler(InvalidCertificateException.class)
    public ResponseEntity<?> invalidCertificateExceptionHandler(InvalidCertificateException e){
        return ResponseEntity.badRequest().body(
                new BaseExceptionDto(400, e.getMessage(), 10400)
        );
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<?> noDataFoundExceptionHandler(NoDataFoundException e){
        return ResponseEntity.status(400).body(
                new BaseExceptionDto(204, e.getMessage(), 10204)
        );
    }

    @ExceptionHandler(UnknownDataBaseException.class)
    public ResponseEntity<?> unknownDatabaseExceptionHandler(UnknownDataBaseException e){
        return ResponseEntity.status(500).body(
                new BaseExceptionDto(500, e.getMessage(), 10500)
        );
    }

}
