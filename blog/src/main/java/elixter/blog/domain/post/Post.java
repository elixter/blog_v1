package elixter.blog.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.hashtag.Hashtag;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
@ToString(exclude = "hashtags")
@EqualsAndHashCode
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private RecordStatus status;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Hashtag> hashtags = new ArrayList<>();

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
    public Post(Long id, String title, String content, String category, String thumbnail, RecordStatus status, LocalDateTime createAt, LocalDateTime updateAt, List<Hashtag> hashtags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.hashtags = hashtags;
    }

    @JsonIgnore
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


