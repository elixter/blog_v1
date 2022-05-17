package elixter.blog.repository.image;

import elixter.blog.domain.image.Image;

import java.util.List;
import java.util.Optional;

public interface ImageRepository {
    Image save(Image image);

    List<Image> findAll();
    List<Image> findByStatus(String status);
    List<Image> findByPostId(Long postId);
    List<Image> findByStoredName(List<String> urlList);

    Optional<Image> findByStoredName(String storedName);

    void relateWithPost(List<Long> idList, Long postId);
}
