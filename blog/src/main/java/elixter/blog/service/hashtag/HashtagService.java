package elixter.blog.service.hashtag;

import elixter.blog.domain.Hashtag;
import elixter.blog.domain.Post;

import java.util.List;
import java.util.Optional;

public interface HashtagService {
    Long createHashtag(Hashtag hashtag);
    List<Long> createHashtags(List<Hashtag> hashtags);
    Optional<Hashtag> findHashtagById(Long id);
    List<Hashtag> findAllHashtag();
    List<Hashtag> findHashtagByTag(String tag);
    List<Hashtag> findHashtagByPostId(Long postId);
    void deleteHashtagById(Long id);
    void deleteHashtagByTag(String tag);
}
