package elixter.blog.domain.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.post.Post;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originName;
    private String storedName;
    private LocalDateTime createAt;
    private RecordStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "images_posts",
            joinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id")
    )
    private Post post;

    public void setPost(Post post) {
        if (this.post != null) {
            this.post.getImages().remove(this);
        }
        this.post = post;

        if (!post.getImages().contains(this)) {
            this.post.addImage(this);
        }
    }

    public Image() {
        createAt = LocalDateTime.now().withNano(0);
    }

    @Builder
    public Image(Long id, String originName, String storedName, LocalDateTime createAt, RecordStatus status) {
        this.id = id;
        this.originName = originName;
        this.storedName = storedName;
        this.createAt = createAt;
        this.status = status;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return storedName.isEmpty() && originName.isEmpty();
    }

    public static Image getEmpty() {
        return Image.builder()
                .storedName("")
                .originName("")
                .build();
    }
}
