package elixter.blog.service.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.hashtag.HashtagCountInterface;
import elixter.blog.repository.hashtag.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository repository;

    @Autowired
    public HashtagServiceImpl(@Qualifier("jpaHashtagRepository") HashtagRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long createHashtag(Hashtag hashtag) {
        return repository.save(hashtag).getId();
    }

    @Override
    public List<Long> createHashtags(List<Hashtag> hashtags) {
        return repository.saveAll(hashtags).stream()
                .map(Hashtag::getId)
                .collect(Collectors.toList());
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
    public List<HashtagCountInterface> searchHashtagsByTag(String tag) {
        return repository.searchTag(tag);
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
