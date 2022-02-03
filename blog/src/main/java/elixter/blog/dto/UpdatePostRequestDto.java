package elixter.blog.dto;

import elixter.blog.domain.Hashtag;
import elixter.blog.domain.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class UpdatePostRequestDto {
    private Long postId;
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private LocalDateTime createAt;
    private List<String> hashtags;

    public Post PostMapping() {
        Post post = new Post();

        post.setTitle(title);
        post.setCategory(category);
        post.setContent(content);
        post.setThumbnail(thumbnail);
        post.setCreateAt(createAt);
        post.setUpdateAt(LocalDateTime.now());

        return post;
    }

    public List<Hashtag> HashtagListMapping() {
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
