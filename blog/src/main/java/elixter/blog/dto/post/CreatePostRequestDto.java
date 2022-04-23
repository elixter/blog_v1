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
@AllArgsConstructor
public class CreatePostRequestDto {
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private List<String> hashtags;
    private List<Long> imageIdList;

    public Post PostMapping() {
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
