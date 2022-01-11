package elixter.blog.post;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemoryPostRepository implements PostRepository {
    private static Map<Long, Post> store = new HashMap<>();

    @Override
    public void save(Post post) {
        store.put(post.getId(), post);
    }

    @Override
    public void update(Post post) {
        store.put(post.getId(), post);
    }

    @Override
    public Post findById(Long id) {
        return store.get(id);
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
    public List<Post> findAllByCategory(String category) {
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
    public List<Post> findAllByHashtag(String hashtag) {
        return null;
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}
