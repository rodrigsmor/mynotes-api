package com.rm.mynotes.service;

import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.utils.config.JwtService;
import com.rm.mynotes.utils.constants.Role;
import com.rm.mynotes.utils.dto.payloads.AuthResponseDTO;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.LoginDTO;
import com.rm.mynotes.utils.dto.requests.SignupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<ResponseDTO> signup(SignupDTO signupDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        var user = UserEntity.builder()
                .fullName(signupDTO.getFullName())
                .email(signupDTO.getEmail())
                .password(passwordEncoder.encode(signupDTO.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        responseDTO.setMessage("Conta criada com ẽxito!");
        responseDTO.setSuccess(true);
        responseDTO.setData(AuthResponseDTO.builder().token(jwtToken).build());

        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<ResponseDTO> login(LoginDTO loginDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            var user = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow();


            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);

            responseDTO.setMessage("Você se conectou com a sua conta!");
            responseDTO.setSuccess(true);
            responseDTO.setData(AuthResponseDTO.builder().token(jwtToken).build());

            return ResponseEntity.ok(responseDTO);
        } catch (BadCredentialsException exception) {
            responseDTO.setSuccess(false);
            responseDTO.setMessage(exception.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        } catch (Exception exception) {
            responseDTO.setSuccess(false);
            responseDTO.setMessage(exception.getMessage());
            responseDTO.setData(exception.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
