package elixter.blog.dto.post;

import elixter.blog.domain.post.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class UpdatePostRequestDto extends AbstractPostDto {

    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private LocalDateTime createAt;

    @Builder
    public UpdatePostRequestDto(Long id, String title, String content, String category, String thumbnail, LocalDateTime createAt, List<String> hashtags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
        this.createAt = createAt;
        this.hashtags = hashtags;
    }

    public Post PostMapping() {
        return Post.builder()
                .id(id)
                .title(title)
                .category(category)
                .content(content)
                .thumbnail(thumbnail)
                .createAt(createAt)
                .updateAt(LocalDateTime.now())
                .hashtags(getHashtagAsInstance())
                .build();
    }
}
