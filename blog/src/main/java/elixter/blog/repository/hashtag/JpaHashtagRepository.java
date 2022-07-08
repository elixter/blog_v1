package elixter.blog.repository.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.hashtag.HashtagCountInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("jpaHashtagRepository")
public class JpaHashtagRepository implements HashtagRepository {

    private final SpringDataJpaHashtagRepository repository;

    @Autowired
    public JpaHashtagRepository(SpringDataJpaHashtagRepository repository) {
        this.repository = repository;
    }

    @Override
    public Hashtag save(Hashtag hashtag) {
        return repository.save(hashtag);
    }

    @Override
    public List<Hashtag> saveAll(List<Hashtag> hashtag) {
        return repository.saveAll(hashtag);
    }

    @Override
    public Optional<Hashtag> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Hashtag> findByTag(String tag) {
        return repository.findByTag(tag);
    }

    @Override
    public List<Hashtag> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Hashtag> findByPostId(Long postId) {
        return repository.findByPostId(postId);
    }

    @Override
    public List<HashtagCountInterface> searchTag(String tag) {
        return repository.searchTag(tag);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByTag(String tag) {
        repository.deleteByTag(tag);
    }

    @Override
    public void deleteByPostId(Long postId) {
        repository.deleteByPostId(postId);
    }
}
