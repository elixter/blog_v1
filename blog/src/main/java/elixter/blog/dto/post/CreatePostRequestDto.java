package elixter.blog.dto.post;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CreatePostRequestDto {
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private List<String> hashtags;
    private List<Long> imageIdList;

    public Post PostMapping() {
        Post post = new Post();

        post.setTitle(title);
        post.setCategory(category);
        post.setContent(content);
        post.setThumbnail(thumbnail);

        return post;
    }

    public List<Hashtag> HashtagListMapping(Long postId) {
        List<Hashtag> result = new ArrayList<>();

        for (String tag : hashtags) {
            Hashtag hashtag = new Hashtag();
            hashtag.setTag(tag);
            hashtag.setPostId(postId);
            result.add(hashtag);
        }

        return result;
    }
}
