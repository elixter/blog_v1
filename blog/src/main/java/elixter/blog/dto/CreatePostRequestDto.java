package elixter.blog.dto;

import elixter.blog.domain.Hashtag;
import elixter.blog.domain.Post;
import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class CreatePostRequestDto {
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private List<String> hashtags;

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
