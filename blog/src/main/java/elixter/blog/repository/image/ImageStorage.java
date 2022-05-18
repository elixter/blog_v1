package elixter.blog.repository.image;

import elixter.blog.domain.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorage {
    Image save(MultipartFile multipartFile);
}
