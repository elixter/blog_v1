package elixter.blog.hashtag;

import java.util.ArrayList;

public interface HashtagService {
    void createHashtag(Hashtag hashtag);
    Hashtag findHashtagById(Long id);
    Hashtag findHashtagByValue(String value);
    ArrayList<Hashtag> findHashtagsByPostId(Long postId);
    ArrayList<Hashtag> findAllHashtags();
}
