package elixter.blog.repository.post;

import elixter.blog.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    void update(Post post);

    Optional<Post> findById(Long id);
    List<Post> findAll(Long offset, Long limit);
    List<Post> findByCategory(String category, Long offset, Long limit);
    List<Post> findByHashtag(String hashtag, Long offset, Long limit);

    void delete(Long id);
}
