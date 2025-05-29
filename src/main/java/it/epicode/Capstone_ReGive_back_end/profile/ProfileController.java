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

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal AppUser user) {
        return ResponseEntity.ok(profileService.getProfile(user.getId()));
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProfileResponse> getUserProfileById(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PutMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
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
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


}