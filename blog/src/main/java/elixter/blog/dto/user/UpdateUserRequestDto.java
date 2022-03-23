package elixter.blog.dto.user;

import elixter.blog.domain.user.User;
import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String name;
    private String loginPw;
    private String email;
    private String profileImage;

    public UpdateUserRequestDto() {
    }

    public UpdateUserRequestDto(String name, String loginPw, String email, String profileImage) {
        this.name = name;
        this.loginPw = loginPw;
        this.email = email;
        this.profileImage = profileImage;
    }

    public User mapping() {
        return new User(name, loginPw, email, profileImage);
    }
}
