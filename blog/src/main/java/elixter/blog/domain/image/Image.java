package elixter.blog.domain.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import elixter.blog.constants.RecordStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Data
public class Image {

    private Long id;
    private String originName;
    private String storedName;
    private LocalDateTime createAt;
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
