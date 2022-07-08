package elixter.blog.repository.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.hashtag.HashtagCountInterface;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository {
    Hashtag save(Hashtag hashtag);
    List<Hashtag> saveAll(List<Hashtag> hashtag);

    Optional<Hashtag> findById(Long id);
    List<Hashtag> findByTag(String tag);
    List<Hashtag> findAll();
    List<Hashtag> findByPostId(Long postId);

    List<HashtagCountInterface> searchTag(String tag);

    void deleteById(Long id);
    void deleteByTag(String tag);
    void deleteByPostId(Long postId);
}
