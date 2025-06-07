package it.epicode.Capstone_ReGive_back_end.beforeAfterPost;


import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "before_after_posts")
public class BeforeAfterPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "media_files", joinColumns = @JoinColumn(name = "post_id"))
    private List<MediaFile> mediaFiles = new ArrayList<>();

    @ManyToOne
    private AppUser author;

    @ManyToMany
    private Set<AppUser> likedBy;

    private LocalDateTime createdAt;


    @ElementCollection(fetch = FetchType.EAGER)
    @OrderBy("createdAt ASC")
    private List<Comment> comments = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }


    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MediaFile {
        private String url;
        private String fileType;
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comment {
        private String authorUsername;
        private String authorEmail;

        @Column(length = 1000)
        private String content;

        private LocalDateTime createdAt;
    }
}