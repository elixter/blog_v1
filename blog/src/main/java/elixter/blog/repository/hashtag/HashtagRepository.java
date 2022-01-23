package elixter.blog.repository.hashtag;

import elixter.blog.domain.Hashtag;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository {
    Hashtag save(Hashtag hashtag);

    Optional<Hashtag> findById(Long id);
    Optional<Hashtag> findByTag(String tag);
    List<Hashtag> findAll();
    List<Hashtag> findByPostId(Long postId);

    void deleteById(Long id);
    void deleteByTag(String tag);
}
