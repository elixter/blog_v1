package elixter.blog.repository.image;

import elixter.blog.domain.image.Image;

import java.util.List;

public interface ImageRepository {
    Image save(Image image);

    List<Image> findAll();
    List<Image> findByStatus(String status);
    List<Image> findByPostId(Long postId);

    void updateStatusById(Long id, String status);
    void updateStatusByIdBatch(List<Long> idList, String status);

    void relateWithPost(List<Long> idList, Long postId);
}
