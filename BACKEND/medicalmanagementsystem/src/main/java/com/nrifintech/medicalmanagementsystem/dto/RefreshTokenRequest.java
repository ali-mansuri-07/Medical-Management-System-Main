package com.nrifintech.medicalmanagementsystem.dto;

public class RefreshTokenRequest {
    private String token;

    @Override
    public String toString() {
        return "RefreshTokenRequest [token=" + token + "]";
    }

    public String getToken() {
        return token;
    }

    public RefreshTokenRequest() {
    }

    public void setToken(String token) {
        this.token = token;
    }
}

