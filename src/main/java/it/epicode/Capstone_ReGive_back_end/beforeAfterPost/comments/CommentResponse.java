package it.epicode.Capstone_ReGive_back_end.beforeAfterPost.comments;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private String authorUsername;
    private String authorEmail;
    private String content;
    private LocalDateTime createdAt;
}
