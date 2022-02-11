package elixter.blog.repository.hashtag;

import elixter.blog.domain.Hashtag;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository {
    Hashtag save(Hashtag hashtag);
    List<Hashtag> batchSave(List<Hashtag> hashtags);

    Optional<Hashtag> findById(Long id);
    List<Hashtag> findByTag(String tag);
    List<Hashtag> findAll();
    List<Hashtag> findByPostId(Long postId);

    void deleteById(Long id);
    void deleteByTag(String tag);
    void deleteByPostId(Long postId);
}
