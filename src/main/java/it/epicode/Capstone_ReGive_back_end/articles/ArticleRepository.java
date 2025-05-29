package it.epicode.Capstone_ReGive_back_end.articles;

import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByUser(AppUser user);
    List<Article> findByUserIdNot(Long userId);
    List<Article> findByUserId(Long userId);


}