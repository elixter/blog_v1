package elixter.blog.dto.post;

import elixter.blog.dto.PageDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GetAllPostsResponseDto implements Serializable {
    private List<GetPostResponseDto> posts;
    private PageDto page;

    public GetAllPostsResponseDto() {
        posts = new ArrayList<>();
    }

    public GetAllPostsResponseDto(Pageable pageable, Long totalElem, Integer totalPage) {
        this.posts = new ArrayList<>();
        this.page = new PageDto(pageable, totalElem, totalPage);
    }
}
