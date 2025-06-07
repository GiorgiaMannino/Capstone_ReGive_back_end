package it.epicode.Capstone_ReGive_back_end.articles;

import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import it.epicode.Capstone_ReGive_back_end.auth.AppUserRepository;
import it.epicode.Capstone_ReGive_back_end.cloudinary.CloudinaryService;
import it.epicode.Capstone_ReGive_back_end.favorites.Favorite;
import it.epicode.Capstone_ReGive_back_end.favorites.FavoriteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CloudinaryService cloudinaryService;
    private final AppUserRepository appUserRepository;
    private final FavoriteRepository favoriteRepository;


    public ArticleResponse createArticle(ArticleRequest request) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : request.getImages()) {
            String url = cloudinaryService.uploadImage(image);
            imageUrls.add(url);
        }

        Article article = Article.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrls(imageUrls)
                .category(request.getCategory())
                .user(user)
                .build();

        articleRepository.save(article);

        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .imageUrls(article.getImageUrls())
                .category(article.getCategory().toString())
                .username(article.getUser().getUsername())
                .userId(article.getUser().getId())
                .build();

    }

    public List<ArticleResponse> getArticlesByCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        List<Article> articles = articleRepository.findByUser(user);

        return articles.stream()
                .map(article -> ArticleResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .description(article.getDescription())
                        .imageUrls(article.getImageUrls())
                        .category(article.getCategory().toString())
                        .username(article.getUser().getUsername())
                        .userId(article.getUser().getId())
                        .build())

                .collect(Collectors.toList());
    }

    public ArticleResponse updateArticle(Long id, ArticleRequest request) throws IOException {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Articolo non trovato"));

        article.setTitle(request.getTitle());
        article.setDescription(request.getDescription());
        article.setCategory(request.getCategory());

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : request.getImages()) {
                String url = cloudinaryService.uploadImage(image);
                imageUrls.add(url);
            }
            article.setImageUrls(imageUrls);
        }

        articleRepository.save(article);

        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .imageUrls(article.getImageUrls())
                .category(article.getCategory().toString())
                .username(article.getUser().getUsername())
                .userId(article.getUser().getId())
                .build();

    }

    public List<ArticleResponse> getAllArticles() {
        List<Article> articles = articleRepository.findAll();

        return articles.stream()
                .map(article -> ArticleResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .description(article.getDescription())
                        .imageUrls(article.getImageUrls())
                        .category(article.getCategory().toString())
                        .username(article.getUser().getUsername())
                        .userId(article.getUser().getId())
                        .build())

                .collect(Collectors.toList());
    }

    public ArticleResponse getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Articolo non trovato con ID: " + id));

        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .imageUrls(article.getImageUrls())
                .category(article.getCategory().toString())
                .username(article.getUser().getUsername())
                .userId(article.getUser().getId())
                .likesCount(favoriteRepository.countByArticle(article))
                .build();
    }


    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            return null;
        }

        String username = auth.getName();
        return appUserRepository.findByEmail(username)
                .map(AppUser::getId)
                .orElse(null);
    }

    public List<ArticleResponse> getArticlesExcludingCurrentUser() {
        Long currentUserId = getCurrentUserId();

        if (currentUserId == null) {
            throw new IllegalStateException("Utente non autenticato.");
        }

        List<Article> articles = articleRepository.findByUserIdNot(currentUserId);

        return articles.stream()
                .map(article -> ArticleResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .description(article.getDescription())
                        .imageUrls(article.getImageUrls())
                        .category(article.getCategory().toString())
                        .username(article.getUser().getUsername())
                        .userId(article.getUser().getId())
                        .likesCount(favoriteRepository.countByArticle(article))
                        .build())
                .collect(Collectors.toList());
    }


    public List<ArticleResponse> getArticlesByUserId(Long userId) {
        List<Article> articles = articleRepository.findByUserId(userId);

        return articles.stream()
                .map(article -> ArticleResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .description(article.getDescription())
                        .category(article.getCategory().toString())
                        .imageUrls(article.getImageUrls())
                        .username(article.getUser().getUsername())
                        .userId(article.getUser().getId())
                        .likesCount(favoriteRepository.countByArticle(article))
                        .build())
                .collect(Collectors.toList());
    }


    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Articolo non trovato con ID: " + id));

        List<Favorite> favoritesToDelete = favoriteRepository.findAll()
                .stream()
                .filter(f -> f.getArticle().getId().equals(id))
                .collect(Collectors.toList());

        favoriteRepository.deleteAll(favoritesToDelete);

        if (article.getImageUrls() != null) {
            for (String imageUrl : article.getImageUrls()) {
                cloudinaryService.deleteImage(imageUrl);
            }
        }
        articleRepository.delete(article);
    }


}
