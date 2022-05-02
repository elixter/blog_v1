package elixter.blog.dto.post;

import elixter.blog.constants.RecordStatusConstants;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CreatePostRequestDto {
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private List<String> hashtags;
    private List<String> imageUrlList;

    @Builder
    public CreatePostRequestDto(String title, String content, String category, String thumbnail, List<String> hashtags, List<String> imageUrlList) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
        this.hashtags = hashtags;
        this.imageUrlList = imageUrlList;
    }

    public Post postMapping() {
        LocalDateTime now = LocalDateTime.now().withNano(0);

        return Post.builder()
                .title(title)
                .category(category)
                .thumbnail(thumbnail)
                .content(content)
                .status(RecordStatusConstants.recordStatusExist)
                .createAt(now)
                .updateAt(now)
                .build();
    }

    public List<Hashtag> hashtagListMapping(Long postId) {
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
