package elixter.blog.repository.image;

import elixter.blog.domain.RecordStatus;
import elixter.blog.domain.image.Image;

import java.util.List;
import java.util.Optional;

public interface ImageRepository {
    Image save(Image image);
    <S extends Image> Iterable<S> saveAll(Iterable<S> entities);

    Optional<Image> findById(Long id);

    List<Image> findAll();
    List<Image> findByStatus(RecordStatus status);
    List<Image> findByPostId(Long postId);
    List<Image> findByStoredName(List<String> urlList);

    Optional<Image> findByStoredName(String storedName);
}
