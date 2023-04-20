package com.rm.mynotes.utils.dto.requests;

import lombok.Data;

@Data
public class SignupDTO {
    private String fullName;
    private String email;
    private String password;
}
