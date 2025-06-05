package it.epicode.Capstone_ReGive_back_end.beforeAfterPost;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/beforeafter")
@RequiredArgsConstructor
public class BeforeAfterPostController {

    private final BeforeAfterPostService postService;

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



    @GetMapping
    public ResponseEntity<List<BeforeAfterPostResponse>> getAllPosts() {
        List<BeforeAfterPost> posts = postService.getAllPosts();
        List<BeforeAfterPostResponse> response = posts.stream()
                .map(BeforeAfterPostResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeforeAfterPostResponse> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(BeforeAfterPostResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @beforeAfterPostService.isAuthor(#id, authentication.name)")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(id, principal.getName()); // Nessun Set.of() richiesto
        return ResponseEntity.ok("Post deleted");
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BeforeAfterPost> likePost(@PathVariable Long id, Principal principal) {
        BeforeAfterPost updatedPost = postService.likePost(id, principal.getName());
        return ResponseEntity.ok(updatedPost);
    }
}
