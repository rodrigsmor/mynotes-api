package com.rm.mynotes.resource;

import com.rm.mynotes.service.AuthService;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.LoginDTO;
import com.rm.mynotes.utils.dto.requests.SignupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthResource {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> signup(@RequestBody SignupDTO signupDTO) {
        return authService.signup(signupDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> signup(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }
}
