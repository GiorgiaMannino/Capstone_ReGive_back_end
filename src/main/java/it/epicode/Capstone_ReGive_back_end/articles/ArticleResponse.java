package it.epicode.Capstone_ReGive_back_end.articles;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArticleResponse {
    private Long id;
    private String title;
    private String description;
    private List<String> imageUrls;
    private String category;
    private String username;
    private Long userId;
    private long likesCount;
}