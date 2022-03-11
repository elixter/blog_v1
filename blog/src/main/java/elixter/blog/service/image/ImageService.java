package elixter.blog.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String save(MultipartFile image) throws IOException;
    byte[] getImageByName(String name);
}
