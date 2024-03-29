package elixter.blog.dto.user;

import elixter.blog.domain.RecordStatus;
import elixter.blog.domain.user.User;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateUserRequestDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String loginPw;
    @Email
    private String email;
    @NotBlank
    private String profileImage;

    public UpdateUserRequestDto() {
    }

    @Builder
    public UpdateUserRequestDto(Long id, String name, String loginPw, String email, String profileImage) {
        this.id = id;
        this.name = name;
        this.loginPw = loginPw;
        this.email = email;
        this.profileImage = profileImage;
    }

    public User mapping() {
        return User.builder()
                .id(id)
                .loginPw(loginPw)
                .name(name)
                .email(email)
                .profileImage(profileImage)
                .status(RecordStatus.exist)
                .build();
    }
}
