package elixter.blog.service.post;

import elixter.blog.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Long createPost(Post post);
    void updatePost(Post post);
    Optional<Post> findPostById(Long id);
    List<Post> findPost(Long offset, Long limit);
    List<Post> findPostByCategory(String category, Long offset, Long limit);
    List<Post> findPostByHashtag(String hashtag, Long offset, Long limit);
    void deletePost(Long id);
}
