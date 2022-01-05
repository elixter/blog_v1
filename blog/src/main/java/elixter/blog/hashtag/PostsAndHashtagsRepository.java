package elixter.blog.hashtag;

import java.util.ArrayList;

public interface PostsAndHashtagsRepository {
    ArrayList<Hashtag> findAllByHashtagId(Long hashtagId);
    ArrayList<Hashtag> findAllByPostId(Long postId);
}
