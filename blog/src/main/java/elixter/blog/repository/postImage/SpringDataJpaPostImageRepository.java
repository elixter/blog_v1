package elixter.blog.repository.postImage;

import elixter.blog.domain.postImage.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaPostImageRepository extends JpaRepository<PostImage, Long> {

    void deleteByPostId(Long postId);
    void deleteByImageId(Long imageId);
}
