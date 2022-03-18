package elixter.blog.dto.auth;

import lombok.Data;

@Data
public class PostLoginRequestDto {
    private String id;
    private String pw;
}
