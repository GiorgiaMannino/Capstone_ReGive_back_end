/*
package it.epicode.Capstone_ReGive_back_end.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile() {
        try {
            return ResponseEntity.ok(userService.getProfile());
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserRequest userRequest) {
        try {
            return ResponseEntity.ok(userService.updateProfile(userRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
*/
