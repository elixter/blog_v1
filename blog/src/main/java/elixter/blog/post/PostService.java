package elixter.blog.post;

import java.util.ArrayList;

public interface PostService {
    void create(Post post);
    void delete(Long id);
    void update(Post post);
    Post findPost(Long id);
    ArrayList<Post> findAllPosts();
    ArrayList<Post> findAllPostsByCategory(String category);
}
