package elixter.blog.dto.post;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class GetPostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<String> hashtags;

    public GetPostResponseDto() {
        hashtags = new ArrayList<>();
    }

    public void postMapping(Post post) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        category = post.getCategory();
        thumbnail = post.getThumbnail();
        createAt = post.getCreateAt();
        updateAt = post.getUpdateAt();
    }

    public void hashtagMapping(List<Hashtag> hashtagList) {
        for (Hashtag hashtag : hashtagList) {
            hashtags.add(hashtag.getTag());
        }
    }
}
