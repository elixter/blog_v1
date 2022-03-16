package elixter.blog.dto.hashtag;

import lombok.Data;

@Data
public class SearchHashtagDto {
    private String tag;
    private Long count;

    public SearchHashtagDto() {
    }

    public SearchHashtagDto(String tag, Long count) {
        this.tag = tag;
        this.count = count;
    }
}
