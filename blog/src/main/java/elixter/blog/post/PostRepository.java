package elixter.blog.post;

import java.util.ArrayList;

public interface PostRepository {
    Post findById(Long id);
    ArrayList<Post> findAllByCategory(String category);
    ArrayList<Post> findAll();

    void save(Post post);

    void update(Post post);

    void deleteById(Long id);
}
