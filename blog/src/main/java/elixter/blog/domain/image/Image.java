package elixter.blog.domain.image;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Image {
    private Long id;
    private String originName;
    private String url;
    private LocalDateTime createAt;
    private String status;

    public Image() {
    }

    public Image(String originName, String url, String status) {
        this.originName = originName;
        this.url = url;
        this.status = status;
    }

    public Image(Long id, String originName, String url, LocalDateTime createAt, String status) {
        this.id = id;
        this.originName = originName;
        this.url = url;
        this.createAt = createAt;
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = true;
        Image _obj = (Image)obj;

        if (obj instanceof Image) {
            if (
                    !this.id.equals(((Image) obj).id) ||
                    !this.url.equals(((Image) obj).url) ||
                    !this.originName.equals(((Image) obj).originName) ||
                    !this.status.equals(((Image) obj).status)
            ) {
                result = false;
            }
        }
        else {
            result = false;
        }

        return result;
    }
}
