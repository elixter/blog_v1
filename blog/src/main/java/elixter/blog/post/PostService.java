package elixter.blog.post;

import java.util.List;

public interface PostService {
    void createPost(Post post);
    void updatePost(Post post);
    Post findPostById(Long id);
    List<Post> findAllPost();
    List<Post> findAllPostByCategory(String category);
    List<Post> findAllPostByHashtag(String hashtag);
}
