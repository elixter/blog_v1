package elixter.blog.dto.hashtag;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SearchHashtag implements SearchHashtagInterface {
    private String tag;
    private Long count;

    public SearchHashtag() {
    }

    public SearchHashtag(String tag, Long count) {
        this.tag = tag;
        this.count = count;
    }
}
