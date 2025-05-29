package it.epicode.Capstone_ReGive_back_end.auth;


import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
