package it.epicode.Capstone_ReGive_back_end.beforeAfterPost;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BeforeAfterPostRequest {

    private String title;
    private String description;

    private List<MultipartFile> files;
}
