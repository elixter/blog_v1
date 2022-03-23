package elixter.blog.dto.user;

import elixter.blog.domain.user.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CreateUserRequestDto {
    @NotBlank private String name;
    @NotBlank private String loginId;
    @NotBlank private String loginPw;
    @Email private String email;

    public CreateUserRequestDto() {
    }

    public CreateUserRequestDto(String name, String loginId, String loginPw, String email) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.email = email;
    }

    public User mapping() {
        return new User(name, loginId, loginPw, email);
    }
}
