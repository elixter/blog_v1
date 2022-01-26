package elixter.blog.service.post;

import elixter.blog.domain.Post;
import elixter.blog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public Long createPost(Post post) {
        postRepository.save(post);

        return post.getId();
    }

    @Override
    public void updatePost(Post post) {
        post.setUpdateAt(LocalDateTime.now());
        postRepository.update(post);
    }

    @Override
    public Optional<Post> findPostById(Long id) {
        Optional<Post> result = postRepository.findById(id);
        System.out.println("reuslt = " + result);

        return result;
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
