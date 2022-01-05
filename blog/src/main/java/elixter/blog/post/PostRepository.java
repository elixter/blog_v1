package elixter.blog.post;

import java.util.ArrayList;

public interface PostRepository {
    // Select
    Post findById(Long id);
    ArrayList<Post> findAllByCategory(String category);
    ArrayList<Post> findAll();

    // Insert
    void save(Post post);

    // Update
    void update(Post post);

    // Delete
    void deleteById(Long id);
}
