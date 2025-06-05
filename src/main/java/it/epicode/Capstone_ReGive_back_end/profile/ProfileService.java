package it.epicode.Capstone_ReGive_back_end.profile;

import io.jsonwebtoken.io.IOException;
import it.epicode.Capstone_ReGive_back_end.articles.ArticleRepository;
import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import it.epicode.Capstone_ReGive_back_end.auth.AppUserRepository;
import it.epicode.Capstone_ReGive_back_end.cloudinary.CloudinaryService; // importa qui
import it.epicode.Capstone_ReGive_back_end.favorites.FavoriteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AppUserRepository appUserRepository;
    private final CloudinaryService cloudinaryService;
    private final ArticleRepository articleRepository;
    private final FavoriteRepository favoriteRepository;



    public List<ProfileResponse> getAllProfiles() {
        List<AppUser> users = appUserRepository.findAll();
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public long countUsers() {
        return appUserRepository.count();
    }


    public ProfileResponse getProfile(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        return toResponse(user);
    }

    public ProfileResponse updateProfileImage(Long userId, MultipartFile imageFile) throws IOException, java.io.IOException {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        // Upload immagine su Cloudinary
        String imageUrl = cloudinaryService.uploadImage(imageFile);

        // Aggiorna URL immagine nel profilo utente
        user.setProfileImage(imageUrl);
        appUserRepository.save(user);

        return toResponse(user);
    }

    public ProfileResponse updateUserProfile(Long userId, ProfileRequest profileRequest) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        if (profileRequest.getDescription() != null) {
            user.setDescription(profileRequest.getDescription());
        }

        if (profileRequest.getProfileImage() != null) {
            user.setProfileImage(profileRequest.getProfileImage());
        }

        appUserRepository.save(user);

        return toResponse(user);
    }



    private ProfileResponse toResponse(AppUser user) {
        return new ProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayUsername(),
                user.getDescription(),
                user.getProfileImage()
        );
    }

   @Transactional
   public void deleteUserProfile(Long userId) {
       AppUser user = appUserRepository.findById(userId)
               .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

       //Elimina tutti i preferiti associati all'utente
       favoriteRepository.deleteByUserId(userId);

       //Elimina tutti gli articoli associati all'utente
       articleRepository.deleteByUserId(userId);

       //Elimina l'immagine del profilo se presente
       if (user.getProfileImage() != null) {
           try {
               cloudinaryService.deleteImage(user.getProfileImage());
           } catch (Exception e) {
               System.out.println("Errore durante eliminazione immagine: " + e.getMessage());
           }
       }

       //Elimina l'utente
       appUserRepository.deleteById(userId);
   }
}

