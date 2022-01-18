package elixter.blog.repository.post;

import elixter.blog.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    void update(Post post);

    Optional<Post> findById(Long id);
    List<Post> findAll();
    List<Post> findByCategory(String category);

    void delete(Long id);
}
