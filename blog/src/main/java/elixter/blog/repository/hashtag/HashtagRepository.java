package elixter.blog.repository.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.dto.hashtag.SearchHashtag;
import elixter.blog.dto.hashtag.SearchHashtagInterface;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository {
    Hashtag save(Hashtag hashtag);
    List<Hashtag> saveBatch(List<Hashtag> hashtag);

    <S extends Hashtag> Iterable<S> saveAll(Iterable<S> entities);

    Optional<Hashtag> findById(Long id);
    List<Hashtag> findByTag(String tag);
    List<Hashtag> findAll();
    List<Hashtag> findByPostId(Long postId);

    List<SearchHashtagInterface> searchTag(String tag);

    void deleteById(Long id);
    void deleteByTag(String tag);
    void deleteByPostId(Long postId);
}
