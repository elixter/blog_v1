package elixter.blog.repository.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorage {
    String save(MultipartFile multipartFile) throws IOException;
    byte[] getByName(String name);
}
