package elixter.blog.post;

import java.util.ArrayList;

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void createPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post findPost(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public ArrayList<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public ArrayList<Post> findAllPostsByCategory(String category) {
        return postRepository.findAllByCategory(category);
    }

    @Override
    public void updatePost(Post post) {
        postRepository.update(post);
    }
}
