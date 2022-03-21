package elixter.blog.dto.user;

import elixter.blog.domain.user.User;
import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String name;
    private String loginId;
    private String loginPw;

    public CreateUserRequestDto() {
    }

    public CreateUserRequestDto(String name, String loginId, String loginPw) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
    }

    public User mapping() {
        return new User(name, loginId, loginPw);
    }
}
