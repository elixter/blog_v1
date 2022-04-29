package elixter.blog.repository.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.dto.hashtag.SearchHashtagDto;

import java.util.List;
import java.util.Optional;

// TODO: Url로 찾는 메소드 필요

public interface HashtagRepository {
    Hashtag save(Hashtag hashtag);
    List<Hashtag> batchSave(List<Hashtag> hashtags);

    Optional<Hashtag> findById(Long id);
    List<Hashtag> findByTag(String tag);
    List<Hashtag> findAll();
    List<Hashtag> findByPostId(Long postId);

    List<SearchHashtagDto> searchTag(String tag, Long offset, Long limit);

    void deleteById(Long id);
    void deleteByTag(String tag);
    void deleteByPostId(Long postId);
}
