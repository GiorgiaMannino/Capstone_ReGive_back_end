package it.epicode.Capstone_ReGive_back_end.articles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // CREAZIONE ARTICOLO
    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public ArticleResponse createArticle(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("category") String category
    ) throws IOException {

        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoria è obbligatoria.");
        }

        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Almeno un'immagine è richiesta.");
        }

        if (images.size() > 6) {
            throw new IllegalArgumentException("Puoi caricare al massimo 6 immagini.");
        }

        ArticleRequest request = new ArticleRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setImages(images);

        try {
            request.setCategory(ArticleCategory.valueOf(category.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Categoria non valida: " + category);
        }

        return articleService.createArticle(request);
    }

    // MODIFICA ARTICOLO
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long id,
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart("category") String category
    ) throws IOException {

        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoria è obbligatoria.");
        }

        if (images != null && images.size() > 6) {
            throw new IllegalArgumentException("Puoi caricare al massimo 6 immagini.");
        }

        ArticleRequest request = new ArticleRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setImages(images);

        try {
            request.setCategory(ArticleCategory.valueOf(category.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Categoria non valida: " + category);
        }

        ArticleResponse updatedArticle = articleService.updateArticle(id, request);
        return ResponseEntity.ok(updatedArticle);
    }

    // ARTICOLI UTENTE CORRENTE
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ArticleResponse>> getMyArticles() {
        return ResponseEntity.ok(articleService.getArticlesByCurrentUser());
    }

    // TUTTI GLI ARTICOLI
    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        List<ArticleResponse> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

// ARTICOLO PER ID
   /* @PreAuthorize("hasRole('USER') and hasRole('ADMIN')")*/

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id) {
        ArticleResponse article = articleService.getArticleById(id);
        return ResponseEntity.ok(article);
    }

    // ARTICOLI DI ALTRI UTENTI
/*    @PreAuthorize("hasRole('USER') and hasRole('ADMIN')")*/
    @GetMapping("/articles/others")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ArticleResponse>> getArticlesExcludingCurrentUser() {
        List<ArticleResponse> articles = articleService.getArticlesExcludingCurrentUser();
        return ResponseEntity.ok(articleService.getArticlesExcludingCurrentUser());
    }

    // ARTICOLI DI UN UTENTE SPECIFICO
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ArticleResponse>> getArticlesByUserId(@PathVariable Long userId) {
        List<ArticleResponse> articles = articleService.getArticlesByUserId(userId);
        return ResponseEntity.ok(articles);
    }

// ELIMINAZIONE ARTICOLO
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }


}
