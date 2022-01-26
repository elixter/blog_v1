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

    // TODO: hashtag DDL value 이름 tag로 바꾸기
    // TODO: hashtag 테이블에 status 컬럼 추가
    public Hashtag() {
    }

    public Hashtag(Long id, String tag, Long postId) {
        this.id = id;
        this.tag = tag;
        this.postId = postId;
    }
}
