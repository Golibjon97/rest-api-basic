package com.epam.esm.exception;

public class TagAlreadyExistException extends RuntimeException{

    public TagAlreadyExistException(String message){
        super(message);
    }
}
