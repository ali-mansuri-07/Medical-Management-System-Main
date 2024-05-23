package com.nrifintech.medicalmanagementsystem.service;

import com.nrifintech.medicalmanagementsystem.dto.UserDTO;
import com.nrifintech.medicalmanagementsystem.dto.UserIdentificationDTO;

public interface UserIdentificationService {

    UserIdentificationDTO convertUserDTOtoUserIdentificationDTO(UserDTO userDTO);
}
