package elixter.blog.dto.image;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ImageUploadResponseDto {
    private String url;
    private String originName;

    public ImageUploadResponseDto(String url, String originName) {
        this.url = url;
        this.originName = originName;
    }
}
