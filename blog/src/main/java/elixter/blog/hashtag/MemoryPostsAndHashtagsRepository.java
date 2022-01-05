package elixter.blog.hashtag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryPostsAndHashtagsRepository implements PostsAndHashtagsRepository {
    private static Map<Long, Long> postIdKeyStore = new HashMap<>();
    private static Map<Long, Long> hashtagIdKeyStore = new HashMap<>();

    @Override
    public ArrayList<Hashtag> findAllByHashtagId(Long hashtagId) {
        return null;
    }

    @Override
    public ArrayList<Hashtag> findAllByPostId(Long postId) {
        return null;
    }
}
