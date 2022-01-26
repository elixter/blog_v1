package elixter.blog.repository.post;

import elixter.blog.domain.Post;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class MemoryPostRepository implements PostRepository {
    private static Map<Long, Post> store = new HashMap<>();
    private static long sequence = 0;

    @PostConstruct
    public void init() {
        System.out.println("MemoryPostRepository");
    }

    @Override
    public Post save(Post post) {
        post.setId(++sequence);
        store.put(post.getId(), post);

        return post;
    }

    @Override
    public void update(Post post) {
        store.put(post.getId(), post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Post> findAll() {
        List<Post> result = new ArrayList<>();

        for (Map.Entry<Long, Post>post : store.entrySet()) {
            result.add(post.getValue());
        }

        return result;
    }

    @Override
    public List<Post> findByCategory(String category) {
        List<Post> result = new ArrayList<>();

        for (Map.Entry<Long, Post>post : store.entrySet()) {
            Post currentPost = post.getValue();

            if (currentPost.getCategory().equals(category)) {
                result.add(currentPost);
            }
        }

        return result;
    }

    @Override
    public List<Post> findByHashtag(String hashtag) {
        return null;
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    public void clearStore() {
        store.clear();
    }
}