package elixter.blog.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import elixter.blog.domain.RecordStatus;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.postImage.PostImage;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
@ToString(exclude = {"hashtags", "postImages"})
@EqualsAndHashCode
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 20)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    private RecordStatus status;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Hashtag> hashtags = new ArrayList<>();

    public void addHashtag(Hashtag hashtag) {
        if (!hashtags.contains(hashtag)) {
            this.hashtags.add(hashtag);
        }

        if (hashtag.getPost() != null) {
            if (!hashtag.getPost().equals(this)) {
                hashtag.setPost(this);
            }
        }
    }

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
    public Post(Long id, String title, String content, String category, String thumbnail, RecordStatus status, LocalDateTime createAt, LocalDateTime updateAt, List<Hashtag> hashtags, List<PostImage> postImages) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;

        if (hashtags != null) {
            this.hashtags.addAll(hashtags);
        }

        if (postImages != null) {
            this.postImages.addAll(postImages);
        }
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


