package it.epicode.Capstone_ReGive_back_end.favorites;

import it.epicode.Capstone_ReGive_back_end.articles.Article;
import it.epicode.Capstone_ReGive_back_end.articles.ArticleRepository;
import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import it.epicode.Capstone_ReGive_back_end.auth.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ArticleRepository articleRepository;
    private final AppUserRepository appUserRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, ArticleRepository articleRepository, AppUserRepository appUserRepository) {
        this.favoriteRepository = favoriteRepository;
        this.articleRepository = articleRepository;
        this.appUserRepository = appUserRepository;
    }

    public FavoriteResponse addFavorite(Long userId, FavoriteRequest request) {
        System.out.println("Aggiungo preferito: userId=" + userId + ", articleId=" + request.getArticleId());
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        Article article = articleRepository.findById(request.getArticleId()).orElseThrow();

        if (favoriteRepository.findByUserAndArticle(user, article).isPresent()) {
            throw new IllegalStateException("Articolo già tra i preferiti.");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .article(article)
                .build();

        Favorite saved = favoriteRepository.save(favorite);
        return toResponse(saved);
    }

    @Transactional  // ✅ IMPORTANTE: abilita la transazione per la rimozione
    public void removeFavorite(Long userId, Long articleId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        Article article = articleRepository.findById(articleId).orElseThrow();

        favoriteRepository.deleteByUserAndArticle(user, article);
    }

    public List<FavoriteResponse> getFavoritesByUser(Long userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        return favoriteRepository.findByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private FavoriteResponse toResponse(Favorite favorite) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .articleId(favorite.getArticle().getId())
                .article(favorite.getArticle())
                .build();
    }
}
