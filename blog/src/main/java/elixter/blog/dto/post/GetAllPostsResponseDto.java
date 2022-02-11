package elixter.blog.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetAllPostsResponseDto {
    private List<GetPostResponseDto> posts;

    public GetAllPostsResponseDto() {
        posts = new ArrayList<>();
    }
}
