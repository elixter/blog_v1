package elixter.blog.service.image;

import elixter.blog.domain.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    Image save(MultipartFile image);
    void relateWithPost(List<Long> imageIdList, Long postId);
    Image getImageByStoredName(String name);
}
