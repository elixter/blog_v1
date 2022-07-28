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

    @Column(columnDefinition = "TEXT")
    private String originName;

    @Column(columnDefinition = "TEXT")
    private String storedName;

    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private RecordStatus status;

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
