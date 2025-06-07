package it.epicode.Capstone_ReGive_back_end.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            AppUser newUser = appUserService.registerUser(
                    registerRequest.getEmail(),
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    Set.of(Role.ROLE_USER)
            );

            String token = jwtTokenUtil.generateToken(newUser);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("id", newUser.getId());
            response.put("username", newUser.getUsername());
            response.put("email", newUser.getEmail());

            return ResponseEntity.ok(response);

        } catch (MultipleFieldsConflictException e) {
            return ResponseEntity.badRequest().body(e.getErrors());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = appUserService.authenticateUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        return ResponseEntity.ok(new AuthResponse(token));
    }


}
