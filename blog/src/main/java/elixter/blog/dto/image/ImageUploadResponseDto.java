package elixter.blog.dto.image;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ImageUploadResponseDto {
    private String url;

    public ImageUploadResponseDto(String url) {
        this.url = url;
    }
}
