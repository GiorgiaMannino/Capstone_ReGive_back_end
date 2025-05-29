/*
package it.epicode.Capstone_ReGive_back_end.users;

import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import it.epicode.Capstone_ReGive_back_end.auth.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository appUserRepository;

    public AppUser getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
    }

    public UserResponse getProfile() {
        AppUser user = getAuthenticatedUser();
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }

    @Transactional
    public UserResponse updateProfile(UserRequest request) {
        AppUser user = getAuthenticatedUser();

        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());

        appUserRepository.save(user);

        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }
}
*/
