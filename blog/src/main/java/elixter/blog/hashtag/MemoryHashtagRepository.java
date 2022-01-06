package elixter.blog.hashtag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryHashtagRepository implements HashtagRepository {

    private static Map<Long, Hashtag> store = new HashMap<>();

    @Override
    public Hashtag findById(Long id) {
        return store.get(id);
    }

    @Override
    public Hashtag findByValue(String value) {
        Hashtag result = new Hashtag(-1L, "", -1L);

        for (Map.Entry<Long, Hashtag> hashtag : store.entrySet()) {
            Hashtag currentHashtag = hashtag.getValue();
            if (currentHashtag.getValue().equals(value)) {
                result.setId(currentHashtag.getId());
                result.setValue(currentHashtag.getValue());
                break;
            }
        }

        return result;
    }

    @Override
    public ArrayList<Hashtag> findAll() {
        ArrayList<Hashtag> result = new ArrayList<>();

        for (Map.Entry<Long, Hashtag> hashtag : store.entrySet()) {
            Hashtag currentHashtag = hashtag.getValue();
            result.add(currentHashtag);
        }

        return result;
    }

    @Override
    public ArrayList<Hashtag> findAllByPostId(Long postId) {
        return null;
    }

    @Override
    public void save(Hashtag hashtag) {
        store.put(hashtag.getId(), hashtag);
    }
}
