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

    /**
     * Registra un nuovo utente con validazione dei campi.
     */
    @Transactional
    public void registerUser(String email, String username, String password, Set<Role> roles) {
        Map<String, String> errors = new HashMap<>();

        // Validazione email
        if (email == null || email.isBlank() || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            errors.put("email", "Email non valida");
        } else if (appUserRepository.existsByEmail(email)) {
            errors.put("email", "Email già in uso");
        }

        // Validazione username
        if (username == null || username.isBlank() || username.length() < 3 || username.length() > 20) {
            errors.put("username", "Username deve avere tra 3 e 20 caratteri");
        } else if (appUserRepository.existsByUsername(username)) {
            errors.put("username", "Username già in uso");
        }

        // Validazione password
        if (!isValidPassword(password)) {
            errors.put("password", "Password non valida. Deve avere almeno 7 caratteri, una maiuscola e un numero.");
        }

        // Se ci sono errori, li stampo per debug e lancio eccezione
        if (!errors.isEmpty()) {
            System.out.println("❌ Errori nella registrazione utente:");
            errors.forEach((k, v) -> System.out.println("- " + k + ": " + v));
            throw new MultipleFieldsConflictException(errors);
        }

        // Creazione e salvataggio utente
        AppUser user = new AppUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);

        appUserRepository.save(user);
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
            // Lancia un'eccezione standard di Spring Security per credenziali errate
            throw new BadCredentialsException("Credenziali non valide");
        }
    }



    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

}

