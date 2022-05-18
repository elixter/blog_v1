package elixter.blog.dto.post;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class GetPostResponseDto extends AbstractPostDto {

    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private static final Long emptyId = -1L;

    public GetPostResponseDto() {
        hashtags = new ArrayList<>();
    }

    public GetPostResponseDto(Post post) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        category = post.getCategory();
        thumbnail = post.getThumbnail();
        createAt = post.getCreateAt();
        updateAt = post.getUpdateAt();
        hashtags = new ArrayList<>();
    }

    public GetPostResponseDto(Post post, List<Hashtag> hashtagList) {
        id = post.getId();
        title = post.getTitle();
        content = post.getContent();
        category = post.getCategory();
        thumbnail = post.getThumbnail();
        createAt = post.getCreateAt();
        updateAt = post.getUpdateAt();

        hashtags = new ArrayList<>();
        for (Hashtag hashtag : hashtagList) {
            hashtags.add(hashtag.getTag());
        }
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

    public boolean isEmpty() {
        return id.equals(emptyId) &&
                title.isEmpty() &&
                category.isEmpty() &&
                content.isEmpty() &&
                thumbnail.isEmpty() &&
                createAt.isEqual(LocalDateTime.MIN) &&
                updateAt.isEqual(LocalDateTime.MIN) &&
                hashtags.isEmpty();
    }

    public static GetPostResponseDto getEmpty() {
        GetPostResponseDto emptyInstance = new GetPostResponseDto();
        emptyInstance.id = emptyId;
        emptyInstance.title = "";
        emptyInstance.category = "";
        emptyInstance.content = "";
        emptyInstance.thumbnail = "";
        emptyInstance.createAt = LocalDateTime.MIN;
        emptyInstance.updateAt = LocalDateTime.MIN;

        return emptyInstance;
    }
}
