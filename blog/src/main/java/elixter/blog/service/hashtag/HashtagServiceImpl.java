package elixter.blog.service.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.hashtag.SearchHashtag;
import elixter.blog.repository.hashtag.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    private final HashtagRepository repository;

    @Override
    public Long createHashtag(Hashtag hashtag) {
        return repository.save(hashtag).getId();
    }

    @Override
    public List<Long> createHashtags(List<Hashtag> hashtags) {
        List<Long> result = new ArrayList<>();
       repository.batchSave(hashtags);

        return result;
    }

    @Override
    public Optional<Hashtag> findHashtagById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Hashtag> findAllHashtag() {
        return repository.findAll();
    }

    @Override
    public List<Hashtag> findHashtagByTag(String tag) {
        return repository.findByTag(tag);
    }

    @Override
    public List<Hashtag> findHashtagByPostId(Long postId) {
        return repository.findByPostId(postId);
    }

    @Override
    public List<SearchHashtag> searchHashtagsByTag(String tag, Long offset, Long limit) {
        return repository.searchTag(tag, offset, limit);
    }

    @Override
    public void deleteHashtagById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteHashtagByTag(String tag) {
        repository.deleteByTag(tag);
    }

    @Override
    public void deleteHashtagByPostId(Long postId) {
        repository.deleteByPostId(postId);
    }
}
