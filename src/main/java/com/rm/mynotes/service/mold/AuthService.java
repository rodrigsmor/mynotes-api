package com.rm.mynotes.service.mold;

import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.LoginDTO;
import com.rm.mynotes.utils.dto.requests.SignupDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ResponseDTO> login(LoginDTO loginDTO);
    ResponseEntity<ResponseDTO> signup(SignupDTO signupDTO);
}
