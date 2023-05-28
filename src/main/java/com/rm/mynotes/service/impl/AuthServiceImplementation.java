package com.rm.mynotes.service.impl;

import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.service.mold.AuthService;
import com.rm.mynotes.utils.config.JwtService;
import com.rm.mynotes.utils.constants.Role;
import com.rm.mynotes.utils.dto.payloads.AuthResponseDTO;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.LoginDTO;
import com.rm.mynotes.utils.dto.requests.SignupDTO;
import com.rm.mynotes.utils.functions.CollectionMethods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private CollectionMethods collectionMethods;

    @Override
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

            return ResponseEntity.accepted().body(responseDTO);
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

    @Override
    public ResponseEntity<ResponseDTO> signup(SignupDTO signupDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            if (userRepository.existsByEmail(signupDTO.getEmail())) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Esse e-mail já está em uso. Conecte-se ou utilize outro e-mail.");

                return ResponseEntity.badRequest().body(responseDTO);
            }

            UserEntity user = UserEntity.builder()
                    .fullName(signupDTO.getFullName())
                    .email(signupDTO.getEmail())
                    .password(passwordEncoder.encode(signupDTO.getPassword()))
                    .role(Role.USER)
                    .collections(collectionMethods.createFavorite(signupDTO.getFullName()))
                    .build();

            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);

            responseDTO.setSuccess(true);
            responseDTO.setMessage("Conta criada com ẽxito!");
            responseDTO.setData(AuthResponseDTO.builder().token(jwtToken).build());

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/signup").toUriString());

            return ResponseEntity.created(uri).body(responseDTO);
        } catch (ConstraintViolationException exception) {
            responseDTO.setMessage(exception.getMessage());
            responseDTO.setSuccess(false);

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
