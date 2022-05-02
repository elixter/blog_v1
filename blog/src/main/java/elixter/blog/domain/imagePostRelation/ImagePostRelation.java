package elixter.blog.domain.imagePostRelation;

import lombok.Data;

@Data
public class ImagePostRelation {
    private Long imageId;
    private Long post_id;

    public ImagePostRelation(Long imageId, Long post_id) {
        this.imageId = imageId;
        this.post_id = post_id;
    }
}
