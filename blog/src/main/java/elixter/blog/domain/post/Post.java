package elixter.blog.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.image.Image;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
@ToString(exclude = {"hashtags", "images"})
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "images_posts",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id")
    )
    private List<Image> images;

    public void addImage(Image image) {
        if (!images.contains(image)) {
            this.images.add(image);
        }

        if (image.getPost() != null) {
            if (!image.getPost().equals(this)) {
                image.setPost(this);
            }
        }
    }

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Hashtag> hashtags;

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
        hashtags = new ArrayList<>();
        images = new ArrayList<>();
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
        hashtags = new ArrayList<>();
        images = new ArrayList<>();
    }

    @Builder
    public Post(Long id, String title, String content, String category, String thumbnail, RecordStatus status, LocalDateTime createAt, LocalDateTime updateAt, List<Hashtag> hashtags, List<Image> images) {
        this.hashtags = new ArrayList<>();
        this.images = new ArrayList<>();

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

        if (images != null) {
            this.images.addAll(images);
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


