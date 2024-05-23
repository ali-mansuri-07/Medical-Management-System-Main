package com.nrifintech.medicalmanagementsystem.Exception;

public class UnauthorizedloginException extends RuntimeException{
    
    private int errorCode;

    public UnauthorizedloginException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
