package it.epicode.Capstone_ReGive_back_end.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String username;
    private String password;

}
