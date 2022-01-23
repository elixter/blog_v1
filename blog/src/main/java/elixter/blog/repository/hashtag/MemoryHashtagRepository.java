package elixter.blog.repository.hashtag;

import elixter.blog.domain.Hashtag;

import java.util.*;

public class MemoryHashtagRepository implements HashtagRepository {
    private static Map<Long, Hashtag> hashtagStore = new HashMap<>();
    private static long sequence = 0;


    @Override
    public Hashtag save(Hashtag hashtag) {
        hashtag.setId(++sequence);
        hashtagStore.put(hashtag.getId(), hashtag);

        return hashtag;
    }

    @Override
    public Optional<Hashtag> findById(Long id) {
        return Optional.ofNullable(hashtagStore.get(id));
    }

    @Override
    public Optional<Hashtag> findByTag(String tag) {
        Hashtag result = null;

        for (Map.Entry<Long, Hashtag> currentEntry : hashtagStore.entrySet()) {
            Hashtag currentHashTag = currentEntry.getValue();
            if (currentHashTag.getTag().equals(tag)) {
                result = currentHashTag;
            }
        }

        return Optional.ofNullable(result);
    }

    @Override
    public List<Hashtag> findAll() {
        List<Hashtag> result = new ArrayList<>();

        for (Map.Entry<Long, Hashtag> currentEntry : hashtagStore.entrySet()) {
            result.add(currentEntry.getValue());
        }

        return result;
    }

    @Override
    public List<Hashtag> findByPostId(Long postId) {
        List<Hashtag> result = new ArrayList<>();

        for (Map.Entry<Long, Hashtag> currentEntry : hashtagStore.entrySet()) {
            Hashtag currentHashtag = currentEntry.getValue();

            if (currentHashtag.getPostId().equals(postId)) {
                result.add(currentHashtag);
            }
        }

        return result;
    }

    @Override
    public void deleteById(Long id) {
        hashtagStore.remove(id);
    }

    @Override
    public void deleteByTag(String tag) {
        List<Hashtag> removeList = new ArrayList<>();

        for (Map.Entry<Long, Hashtag> currentEntry : hashtagStore.entrySet()) {
            Hashtag currentHashtag = currentEntry.getValue();
            if (currentHashtag.getTag().equals(tag)) {
                removeList.add(currentHashtag);
            }
        }

        removeList.forEach(hashtag -> hashtagStore.remove(hashtag.getId()));
    }
}
