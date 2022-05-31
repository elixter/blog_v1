package elixter.blog.dto.post;

import elixter.blog.domain.post.Post;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class UpdatePostRequestDto extends AbstractPostDto {

    @Length(max = 50)
    private String title;

    private String content;

    @NotBlank
    private String category;

    private String thumbnail;

    private LocalDateTime createAt;
    private List<String> imageUrlList;

    @Builder
    public UpdatePostRequestDto(Long id, String title, String content, String category, String thumbnail, LocalDateTime createAt, List<String> hashtags, List<String> imageUrlList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
        this.createAt = createAt;
        this.hashtags = hashtags;
        this.imageUrlList = imageUrlList;
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
