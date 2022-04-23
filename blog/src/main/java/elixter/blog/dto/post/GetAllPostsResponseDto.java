package elixter.blog.dto.post;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GetAllPostsResponseDto {
    private List<GetPostResponseDto> posts;

    public GetAllPostsResponseDto() {
        posts = new ArrayList<>();
    }
}
