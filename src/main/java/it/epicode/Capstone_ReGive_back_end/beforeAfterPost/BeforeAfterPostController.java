package it.epicode.Capstone_ReGive_back_end.beforeAfterPost;


import it.epicode.Capstone_ReGive_back_end.beforeAfterPost.comments.CommentRequest;
import it.epicode.Capstone_ReGive_back_end.beforeAfterPost.comments.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/beforeafter")
@RequiredArgsConstructor
public class BeforeAfterPostController {

    private final BeforeAfterPostService postService;

    // CARICAMENTO POST
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> uploadPost(
            @RequestParam String title,
            @RequestParam String description,
            @RequestPart("files") List<MultipartFile> files,
            Principal principal
    ) throws IOException {
        postService.createPost(title, description, files, principal.getName());
        return ResponseEntity.ok("Post created");
    }


    // OTTERRE POST
    @GetMapping
    public ResponseEntity<List<BeforeAfterPostResponse>> getAllPosts() {
        List<BeforeAfterPost> posts = postService.getAllPosts();
        List<BeforeAfterPostResponse> response = posts.stream()
                .map(BeforeAfterPostResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    // OTTENERE POST PER ID
    @GetMapping("/{id}")
    public ResponseEntity<BeforeAfterPostResponse> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(BeforeAfterPostResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ELIMINARE POST
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @beforeAfterPostService.isAuthor(#id, authentication.name)")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(id, principal.getName()); // Nessun Set.of() richiesto
        return ResponseEntity.ok("Post deleted");
    }

    // LIKE POST
    @PostMapping("/{id}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BeforeAfterPost> likePost(@PathVariable Long id, Principal principal) {
        BeforeAfterPost updatedPost = postService.likePost(id, principal.getName());
        return ResponseEntity.ok(updatedPost);
    }

    // COMMENTO POST
    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        @RequestBody CommentRequest request,
                                        Principal principal) {
        String content = request.getContent();
        postService.addComment(id, content, principal.getName());
        return ResponseEntity.ok("Comment added");
    }

    // OTTENERE COMMENTI
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id) {
        List<CommentResponse> response = postService.getComments(id).stream().map(comment ->
                new CommentResponse(
                        comment.getAuthorUsername(),
                        comment.getAuthorEmail(),
                        comment.getContent(),
                        comment.getCreatedAt()
                )
        ).toList();

        return ResponseEntity.ok(response);
    }


}
