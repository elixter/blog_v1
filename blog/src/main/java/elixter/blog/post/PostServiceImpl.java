package elixter.blog.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void createPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void updatePost(Post post) {
        postRepository.update(post);
    }

    @Override
    public Post findPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAllPost() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAllPostByCategory(String category) {
        return postRepository.findAllByCategory(category);
    }

    @Override
    public List<Post> findAllPostByHashtag(String hashtag) {
        return postRepository.findAllByHashtag(hashtag);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.delete(id);
    }
}
