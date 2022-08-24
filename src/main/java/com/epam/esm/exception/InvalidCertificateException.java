package com.epam.esm.exception;

public class InvalidCertificateException extends RuntimeException{
    public InvalidCertificateException(String message){
        super(message);
    }
}
