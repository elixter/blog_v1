package elixter.blog.dto.post;

import elixter.blog.constants.RecordStatusConstants;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import lombok.*;

import java.time.LocalDateTime;
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
    private List<Long> imageIdList;

    @Builder
    public CreatePostRequestDto(String title, String content, String category, String thumbnail, List<String> hashtags, List<Long> imageIdList) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
        this.hashtags = hashtags;
        this.imageIdList = imageIdList;
    }

    public Post postMapping() {
        return Post.builder()
                .title(title)
                .category(category)
                .thumbnail(thumbnail)
                .content(content)
                .status(RecordStatusConstants.recordStatusExist)
                .createAt(LocalDateTime.now().withNano(0))
                .updateAt(LocalDateTime.now().withNano(0))
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
