package it.epicode.Capstone_ReGive_back_end.favorites;

import it.epicode.Capstone_ReGive_back_end.articles.Article;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteResponse {
    private Long id;
    private Long userId;
    private Long articleId;
    private Article article;

}
