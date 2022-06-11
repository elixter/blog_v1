package elixter.blog.domain.hashtag;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HashtagCount implements HashtagCountInterface {
    private String tag;
    private Long count;

    public HashtagCount() {
    }

    public HashtagCount(String tag, Long count) {
        this.tag = tag;
        this.count = count;
    }
}
