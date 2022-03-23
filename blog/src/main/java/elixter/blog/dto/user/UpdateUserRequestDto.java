package elixter.blog.dto.user;

import elixter.blog.domain.user.User;
import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String loginPw;
    private String email;
    private String profileImage;

    public UpdateUserRequestDto() {
    }

    public UpdateUserRequestDto(String loginPw, String email, String profileImage) {
        this.loginPw = loginPw;
        this.email = email;
        this.profileImage = profileImage;
    }

    public User mapping() {
        return new User(loginPw, email, profileImage);
    }
}
