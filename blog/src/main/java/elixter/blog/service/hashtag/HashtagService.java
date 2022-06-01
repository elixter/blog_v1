package elixter.blog.service.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.dto.hashtag.SearchHashtagInterface;

import java.util.List;
import java.util.Optional;

public interface HashtagService {
    Long createHashtag(Hashtag hashtag);
    List<Long> createHashtags(List<Hashtag> hashtags);
    Optional<Hashtag> findHashtagById(Long id);
    List<Hashtag> findAllHashtag();
    List<Hashtag> findHashtagByTag(String tag);
    List<Hashtag> findHashtagByPostId(Long postId);
    List<SearchHashtagInterface> searchHashtagsByTag(String tag);
    void deleteHashtagById(Long id);
    void deleteHashtagByTag(String tag);
    void deleteHashtagByPostId(Long postId);
}
