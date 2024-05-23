package com.nrifintech.medicalmanagementsystem.dto;

public class JwtAuthenticationResponse {
    private String token;
    private String refreshToken;

    private UserIdentificationDTO userIdentificationDTO;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public JwtAuthenticationResponse() {
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public UserIdentificationDTO getUserIdentificationDTO() {
        return userIdentificationDTO;
    }
    public void setUserIdentificationDTO(UserIdentificationDTO userIdentificationDTO) {
        this.userIdentificationDTO = userIdentificationDTO;
    }
}

