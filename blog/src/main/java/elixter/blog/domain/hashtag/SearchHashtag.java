package elixter.blog.domain.hashtag;

import lombok.Data;

@Data
public class SearchHashtag {
    private String tag;
    private Long count;

    public SearchHashtag() {
    }

    public SearchHashtag(String tag, Long count) {
        this.tag = tag;
        this.count = count;
    }
}
