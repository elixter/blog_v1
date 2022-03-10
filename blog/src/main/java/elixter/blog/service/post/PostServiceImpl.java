package elixter.blog.service.post;

import elixter.blog.domain.post.Post;
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
    public List<Post> findPost(Long offset, Long limit) {
        return postRepository.findAll(offset, limit);
    }

    @Override
    public List<Post> findPostByCategory(String category, Long offset, Long limit) {
        return postRepository.findByCategory(category, offset, limit);
    }

    @Override
    public List<Post> findPostByHashtag(String hashtag, Long offset, Long limit) {
        return postRepository.findByHashtag(hashtag, offset, limit);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.delete(id);
    }
}
