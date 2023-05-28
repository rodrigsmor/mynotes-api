package com.rm.mynotes.service.mold;

import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface UserService {
    ResponseEntity<ResponseDTO> getUserOverview(Authentication authentication);
}
