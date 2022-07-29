package elixter.blog.repository.postImage;

import elixter.blog.domain.postImage.PostImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaPostImageRepository implements PostImageRepository {

    private final SpringDataJpaPostImageRepository repository;

    @Autowired
    public JpaPostImageRepository(SpringDataJpaPostImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public PostImage save(PostImage postImage) {
        return repository.save(postImage);
    }

    @Override
    public List<PostImage> saveAll(List<PostImage> postImages) {
        return repository.saveAll(postImages);
    }

    @Override
    public void deleteByPostId(Long postId) {
        repository.deleteByPostId(postId);
    }

    @Override
    public void deleteByImageId(Long imageId) {
        repository.deleteByImageId(imageId);
    }
}
