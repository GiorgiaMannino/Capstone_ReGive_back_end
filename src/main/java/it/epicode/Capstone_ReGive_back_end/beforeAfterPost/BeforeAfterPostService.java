package it.epicode.Capstone_ReGive_back_end.beforeAfterPost;

import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import it.epicode.Capstone_ReGive_back_end.auth.AppUserRepository;
import it.epicode.Capstone_ReGive_back_end.cloudinary.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BeforeAfterPostService {

    private final BeforeAfterPostRepository postRepository;
    private final AppUserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public BeforeAfterPost createPost(String title, String description, List<MultipartFile> files, String userEmail) throws IOException {
        AppUser author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<BeforeAfterPost.MediaFile> mediaFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            String url = cloudinaryService.uploadImage(file);

            // Determina tipo file (image/video)
            String fileType = "";
            if (file.getContentType() != null && file.getContentType().startsWith("image")) {
                fileType = "image";
            } else if (file.getContentType() != null && file.getContentType().startsWith("video")) {
                fileType = "video";
            }

            mediaFiles.add(new BeforeAfterPost.MediaFile(url, fileType));
        }

        BeforeAfterPost post = BeforeAfterPost.builder()
                .title(title)
                .description(description)
                .mediaFiles(mediaFiles)
                .author(author)
                .likedBy(new HashSet<>())
                .build();

        return postRepository.save(post);
    }

    public List<BeforeAfterPost> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<BeforeAfterPost> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public void deletePost(Long postId, String userEmail, Set<String> userRoles) {
        BeforeAfterPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean isAuthor = post.getAuthor().getEmail().equals(userEmail);
        boolean isAdmin = userRoles.contains("ROLE_ADMIN");

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to delete this post");
        }

        // Elimina i file da Cloudinary
        for (BeforeAfterPost.MediaFile mediaFile : post.getMediaFiles()) {
            cloudinaryService.deleteImage(mediaFile.getUrl());
        }

        postRepository.delete(post);
    }

    public BeforeAfterPost likePost(Long postId, String userEmail) {
        BeforeAfterPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        AppUser user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<AppUser> likes = post.getLikedBy();
        if (likes.contains(user)) {
            likes.remove(user);
        } else {
            likes.add(user);
        }

        post.setLikedBy(likes);
        return postRepository.save(post);
    }

    public boolean isAuthor(Long postId, String userEmail) {
        BeforeAfterPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getAuthor().getEmail().equals(userEmail);
    }

}
