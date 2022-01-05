package elixter.blog.hashtag;

import java.util.ArrayList;

public interface HashtagRepository {
    // Select
    Hashtag findById(Long id);
    Hashtag findByValue(String value);
    ArrayList<Hashtag> findAll();
    ArrayList<Hashtag> findAllByPostId(Long postId);

    // Insert
    void save(Hashtag hashtag);
}
