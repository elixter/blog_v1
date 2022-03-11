package elixter.blog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Image {
    private Long id;
    private String originName;
    private String url;
    private String status;

    public Image() {
    }

    public Image(String originName, String url, String status) {
        this.originName = originName;
        this.url = url;
        this.status = status;
    }

    public Image(Long id, String originName, String url, String status) {
        this.id = id;
        this.originName = originName;
        this.url = url;
        this.status = status;
    }
}
