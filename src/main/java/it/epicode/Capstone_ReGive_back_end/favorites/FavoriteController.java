package it.epicode.Capstone_ReGive_back_end.favorites;

import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // AGGIUNGI PREFERITO
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FavoriteResponse> addFavorite(
            @AuthenticationPrincipal AppUser user,
            @RequestBody FavoriteRequest request) {
        return ResponseEntity.ok(favoriteService.addFavorite(user.getId(), request));
    }

    // RIMUOVI PREFERITO
    @DeleteMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeFavorite(
            @AuthenticationPrincipal AppUser user,
            @RequestParam Long articleId) {
        favoriteService.removeFavorite(user.getId(), articleId);
        return ResponseEntity.noContent().build();
    }

    // OTTIENI PREFERITI
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<FavoriteResponse> getMyFavorites(@AuthenticationPrincipal AppUser user) {
        return favoriteService.getFavoritesByUser(user.getId());
    }
}
