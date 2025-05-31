package it.epicode.Capstone_ReGive_back_end.cloudinary;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Cloudinary.asMap(
                "folder", "FS1024",
                "public_id", file.getOriginalFilename()
        ));
        return uploadResult.get("secure_url").toString();
    }

    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, Cloudinary.asMap());
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'eliminazione dell'immagine da Cloudinary: " + e.getMessage(), e);
        }
    }

    private String extractPublicId(String imageUrl) {
        try {
            String[] parts = imageUrl.split("/");
            int folderIndex = -1;

            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("upload")) {
                    folderIndex = i + 2;
                    break;
                }
            }

            if (folderIndex != -1 && folderIndex < parts.length) {
                StringBuilder publicIdBuilder = new StringBuilder();
                for (int i = folderIndex; i < parts.length; i++) {
                    String part = parts[i];
                    if (part.contains(".")) {
                        publicIdBuilder.append(part.substring(0, part.lastIndexOf('.')));
                    } else {
                        publicIdBuilder.append(part);
                    }
                    if (i < parts.length - 1) publicIdBuilder.append("/");
                }
                return publicIdBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}




