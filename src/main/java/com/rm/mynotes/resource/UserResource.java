package com.rm.mynotes.resource;

import com.rm.mynotes.service.mold.UserService;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserResource {
    @Autowired
    private UserService userService;

    @GetMapping("/api/user/overview")
    public ResponseEntity<ResponseDTO> getUserOverview(Authentication authentication) {
        return userService.getUserOverview(authentication);
    }
}
