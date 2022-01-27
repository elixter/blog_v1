package elixter.blog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Hashtag {
    private Long id;
    private String tag;
    private Long postId;

    public Hashtag() {
    }

    public Hashtag(Long id, String tag, Long postId) {
        this.id = id;
        this.tag = tag;
        this.postId = postId;
    }
}
