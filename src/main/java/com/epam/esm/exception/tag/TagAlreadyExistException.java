package com.epam.esm.exception.tag;

public class TagAlreadyExistException extends RuntimeException{

    public TagAlreadyExistException(String message){
        super(message);
    }
}
