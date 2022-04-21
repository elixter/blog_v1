package elixter.blog.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    String save(MultipartFile image) throws IOException;
    void relateWithPost(List<Long> imageIdList, Long postId);
    byte[] getImageByName(String name);
}
