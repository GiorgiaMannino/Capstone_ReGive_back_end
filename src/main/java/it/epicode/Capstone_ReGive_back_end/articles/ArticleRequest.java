package it.epicode.Capstone_ReGive_back_end.articles;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ArticleRequest {
    private String title;
    private String description;
    private List<MultipartFile> images;
    private ArticleCategory category;
}
