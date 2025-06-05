package it.epicode.Capstone_ReGive_back_end.profile;

import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.util.List;


@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {
        List<ProfileResponse> profiles = profileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }


    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal AppUser user) {
        return ResponseEntity.ok(profileService.getProfile(user.getId()));
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ProfileResponse> getUserProfileById(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PutMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
/*    @PreAuthorize("hasRole('USER') and hasRole('ADMIN')")*/
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ProfileResponse> updateProfileImage(
            @AuthenticationPrincipal AppUser user,
            @RequestPart("file") MultipartFile file) {
        try {
            ProfileResponse updatedProfile = profileService.updateProfileImage(user.getId(), file);
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal AppUser user,
            @RequestBody ProfileRequest profileRequest) {

        ProfileResponse updatedProfile = profileService.updateUserProfile(user.getId(), profileRequest);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteProfile(@AuthenticationPrincipal AppUser user) {
        try {
            profileService.deleteUserProfile(user.getId());
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace(); // Per il debug
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOtherUserProfile(
            @PathVariable Long userId,
            @AuthenticationPrincipal AppUser currentUser) {
        try {
            // L’admin NON può eliminare sé stesso
            if (currentUser.getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            profileService.deleteUserProfile(userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }



}