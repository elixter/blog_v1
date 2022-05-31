package elixter.blog.dto.post;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractPostDto implements Serializable {

    protected Long id;

    protected List<String> hashtags = new ArrayList<>();


    protected List<Hashtag> getHashtagAsInstance() {
        List<Hashtag> result = new ArrayList<>();

        if (hashtags != null) {
            for (String tag : hashtags) {
                Hashtag hashtag = new Hashtag();
                hashtag.setTag(tag);
                hashtag.setPost(Post.builder().id(id).build());
                result.add(hashtag);
            }
        }

        return result;
    }
}
