package elixter.blog.repository.image;

import elixter.blog.domain.image.Image;

import java.util.List;
import java.util.Optional;


// TODO: Url로 찾는 메소드 필요

public interface ImageRepository {
    Image save(Image image);

    List<Image> findAll();
    List<Image> findByStatus(String status);
    List<Image> findByPostId(Long postId);
    Optional<Image> findByUrl(String url);
    List<Image> findByUrlBatch(List<String> urlList);

    void updateStatusById(Long id, String status);
    void updateStatusByIdBatch(List<Long> idList, String status);

    void relateWithPost(List<Long> idList, Long postId);
}
