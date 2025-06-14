package it.epicode.Capstone_ReGive_back_end.auth;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Transactional
    public AppUser registerUser(String email, String username, String password, Set<Role> roles) {
        Map<String, String> errors = new HashMap<>();

        if (email == null || email.isBlank() || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            errors.put("email", "Email non valida");
        } else if (appUserRepository.existsByEmail(email)) {
            errors.put("email", "Email già in uso");
        }

        if (username == null || username.isBlank() || username.length() < 3 || username.length() > 20) {
            errors.put("username", "Username deve avere tra 3 e 20 caratteri");
        } else if (appUserRepository.existsByUsername(username)) {
            errors.put("username", "Username già in uso");
        }

        if (!isValidPassword(password)) {
            errors.put("password", "Password non valida. Deve avere almeno 7 caratteri, una maiuscola e un numero.");
        }

        if (!errors.isEmpty()) {
            System.out.println("Errori nella registrazione utente:");
            errors.forEach((k, v) -> System.out.println("- " + k + ": " + v));
            throw new MultipleFieldsConflictException(errors);
        }

        AppUser user = new AppUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);

        AppUser savedUser = appUserRepository.save(user);
        return savedUser;
    }

    private boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 7 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*");
    }


    public String authenticateUser(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Credenziali non valide");
        }
    }


    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

}

