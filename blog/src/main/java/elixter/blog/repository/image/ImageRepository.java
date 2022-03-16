package elixter.blog.repository.image;

import elixter.blog.domain.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageRepository {
    Image save(MultipartFile multipartFile) throws IOException;
    byte[] get(String name);
}
