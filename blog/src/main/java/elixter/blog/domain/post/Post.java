package elixter.blog.domain.post;

import elixter.blog.constants.RecordStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Post {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private RecordStatus status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private static final Long emptyId = -1L;

    public Post() {
        status = RecordStatus.exist;
        createAt = LocalDateTime.now().withNano(0);
        updateAt = LocalDateTime.now().withNano(0);
    }

    public Post(Long id, String title, String content, String category, String thumbnail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
    }

    @Builder
    public Post(Long id, String title, String content, String category, String thumbnail, RecordStatus status, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public boolean isEmpty() {
        return id.equals(emptyId) &&
                title.isEmpty() &&
                content.isEmpty() &&
                category.isEmpty() &&
                thumbnail.isEmpty() &&
                createAt.isEqual(LocalDateTime.MIN) &&
                updateAt.isEqual(LocalDateTime.MIN);

    }

    public static Post emptyPost() {
        Post result = new Post();
        result.id = emptyId;
        result.title = "";
        result.content = "";
        result.category = "";
        result.thumbnail = "";
        result.createAt = LocalDateTime.MIN;
        result.updateAt = LocalDateTime.MIN;

        return result;
    }

}


