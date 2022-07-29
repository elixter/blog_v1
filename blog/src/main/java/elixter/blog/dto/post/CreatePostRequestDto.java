package elixter.blog.dto.post;

import elixter.blog.domain.RecordStatus;
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
@NoArgsConstructor
public class CreatePostRequestDto extends AbstractPostDto {

    @Length(max = 50)
    private String title;

    private String content;

    @NotBlank
    private String category;

    private String thumbnail;
    private List<String> imageUrlList = new ArrayList<>();

    @Builder
    public CreatePostRequestDto(String title, String content, String category, String thumbnail, List<String> hashtags, List<String> imageUrlList) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
        this.hashtags = hashtags;

        if (imageUrlList != null) {
            this.imageUrlList.addAll(imageUrlList);
        }
    }

    public Post postMapping() {
        LocalDateTime now = LocalDateTime.now().withNano(0);

        Post post = Post.builder()
                .id(id)
                .title(title)
                .category(category)
                .content(content)
                .thumbnail(thumbnail)
                .hashtags(new ArrayList<>())
                .createAt(now)
                .updateAt(now)
                .status(RecordStatus.exist)
                .build();

        return post;
    }
}
