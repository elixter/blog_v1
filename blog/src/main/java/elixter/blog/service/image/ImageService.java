package elixter.blog.service.image;

import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    Image save(MultipartFile image);
    void relateWithPost(List<Image> images, Post post);
    Image getImageByStoredName(String name);
}
