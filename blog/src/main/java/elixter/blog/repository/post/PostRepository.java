package elixter.blog.repository.post;

import elixter.blog.domain.RecordStatus;
import elixter.blog.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    void update(Post post);

    Optional<Post> findById(Long id);
    Optional<Post> findByIdAndStatus(Long id, RecordStatus status);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByStatus(RecordStatus status, Pageable pageable);
    Page<Post> findByCategory(String category, Pageable pageable);
    Page<Post> findByCategoryAndStatus(String category, RecordStatus status, Pageable pageable);
    Page<Post> findByHashtag(String hashtag, Pageable pageable);
    Page<Post> findByHashtagAndStatus(String hashtag, RecordStatus status, Pageable pageable);

    void delete(Long id);
}
