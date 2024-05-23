package com.nrifintech.medicalmanagementsystem.Exception;

public class UnauthorizedAccessException extends RuntimeException{
    
    private int errorCode;

    public UnauthorizedAccessException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
