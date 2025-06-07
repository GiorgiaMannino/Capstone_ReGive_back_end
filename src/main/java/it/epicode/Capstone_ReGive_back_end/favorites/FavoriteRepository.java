package it.epicode.Capstone_ReGive_back_end.favorites;

import it.epicode.Capstone_ReGive_back_end.articles.Article;
import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(AppUser user);
    Optional<Favorite> findByUserAndArticle(AppUser user, Article article);
    void deleteByUserAndArticle(AppUser user, Article article);
    void deleteByUserId(Long userId);
    long countByArticle(Article article);

}
