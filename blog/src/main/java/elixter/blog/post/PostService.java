package elixter.blog.post;

import java.util.ArrayList;

public interface PostService {
    void createPost(Post post);
    void deletePost(Long id);
    void updatePost(Post post);
    Post findPost(Long id);
    ArrayList<Post> findAllPosts();
    ArrayList<Post> findAllPostsByCategory(String category);
}
