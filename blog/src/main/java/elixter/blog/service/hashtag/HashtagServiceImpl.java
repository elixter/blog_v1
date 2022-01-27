package elixter.blog.service.hashtag;

import elixter.blog.domain.Hashtag;
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
        hashtags.forEach(hashtag -> result.add(repository.save(hashtag).getId()));

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
    public void deleteHashtagById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteHashtagByTag(String tag) {
        repository.deleteByTag(tag);
    }
}
