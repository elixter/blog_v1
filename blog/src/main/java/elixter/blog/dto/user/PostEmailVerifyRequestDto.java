package elixter.blog.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class PostEmailVerifyRequestDto {

    private String email;

    public PostEmailVerifyRequestDto(String email) {
        this.email = email;
    }
}
