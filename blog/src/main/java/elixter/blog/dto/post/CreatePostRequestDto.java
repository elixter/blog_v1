package elixter.blog.dto.post;

import elixter.blog.constants.RecordStatus;
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
public class CreatePostRequestDto extends AbstractPostDto {

    private String title;
    private String content;
    private String category;
    private String thumbnail;
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
                .status(RecordStatus.exist)
                .createAt(now)
                .updateAt(now)
                .hashtags(getHashtagAsInstance())
                .build();
    }
}
