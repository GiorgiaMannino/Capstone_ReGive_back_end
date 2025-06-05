package it.epicode.Capstone_ReGive_back_end.beforeAfterPost;

import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class BeforeAfterPostResponse {

    private Long id;
    private String title;
    private String description;
    private List<MediaDTO> mediaFiles;
    private String authorEmail;
    private String authorUsername;
    private int likesCount;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class MediaDTO {
        private String url;
        private String fileType;
    }

    public static BeforeAfterPostResponse fromEntity(BeforeAfterPost post) {
        return BeforeAfterPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .mediaFiles(post.getMediaFiles()
                        .stream()
                        .map(m -> new MediaDTO(m.getUrl(), m.getFileType()))
                        .collect(Collectors.toList()))
                .authorEmail(post.getAuthor().getEmail())
                .authorUsername(post.getAuthor().getDisplayUsername())
                .likesCount(post.getLikedBy() != null ? post.getLikedBy().size() : 0)
                .createdAt(post.getCreatedAt())
                .build();
    }
}
