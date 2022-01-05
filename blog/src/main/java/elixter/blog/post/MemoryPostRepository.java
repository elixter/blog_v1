package elixter.blog.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryPostRepository implements PostRepository {

    static private Map<Long, Post> store = new HashMap<>();

    @Override
    public Post findById(Long id) {
        return store.get(id);
    }

    @Override
    public ArrayList<Post> findAllByCategory(String category) {
        ArrayList<Post> result = new ArrayList<>();

        for (Map.Entry<Long, Post> post : store.entrySet()) {
            Post currentPost = post.getValue();
            if (currentPost.getCategory() == category) {
                result.add(currentPost);
            }
        }

        return result;
    }

    @Override
    public ArrayList<Post> findAll() {
        ArrayList<Post> result = new ArrayList<>();

        for (Map.Entry<Long, Post> post : store.entrySet()) {
            Post currentPost = post.getValue();
            result.add(currentPost);
        }

        return result;
    }

    @Override
    public void save(Post post) {
        store.put(post.getId(), post);
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public void update(Post post) {
        store.put(post.getId(), post);
    }
}
