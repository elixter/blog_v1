package elixter.blog.repository.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageRepository {
    String save(MultipartFile image) throws IOException;
}
