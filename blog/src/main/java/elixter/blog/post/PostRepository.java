package elixter.blog.post;

import java.util.List;

public interface PostRepository {
    void save(Post post);
    void update(Post post);

    Post findById(Long id);
    List<Post> findAll();
    List<Post> findAllByCategory(String category);
    List<Post> findAllByHashtag(String hashtag);

    void delete(Long id);
}
