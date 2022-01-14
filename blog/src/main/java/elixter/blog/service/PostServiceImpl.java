package elixter.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public void createPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void updatePost(Post post) {
        postRepository.update(post);
    }

    @Override
    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findPost() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findPostByCategory(String category) {
        return postRepository.findByCategory(category);
    }

    @Override
    public List<Post> findPostByHashtag(String hashtag) {
        return postRepository.findByHashtag(hashtag);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.delete(id);
    }
}
