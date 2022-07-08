package elixter.blog.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Data
public class PageDto implements Serializable {

    private Pageable pageable;
    private Long totalElement;
    private Integer totalPage;

    public PageDto(Pageable pageable, Long totalElement, Integer totalPage) {
        this.pageable = pageable;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }
}
