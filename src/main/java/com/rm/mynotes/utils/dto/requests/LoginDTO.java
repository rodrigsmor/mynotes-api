package com.rm.mynotes.utils.dto.requests;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;
}
