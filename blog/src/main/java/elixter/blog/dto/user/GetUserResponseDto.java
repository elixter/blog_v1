package elixter.blog.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import elixter.blog.domain.user.User;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class GetUserResponseDto {
    private String name;
    private String loginId;
    private String email;
    private String profileImage;

    public GetUserResponseDto() {
    }

    @Builder
    public GetUserResponseDto(String name, String loginId, String email, String profileImage) {
        this.name = name;
        this.loginId = loginId;
        this.email = email;
        this.profileImage = profileImage;
    }

    public GetUserResponseDto(User user) {
        this.name = user.getName();
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
    }

    public void mapping(User user) {
        this.name = user.getName();
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return name.isEmpty() &&
                loginId.isEmpty() &&
                email.isEmpty() &&
                profileImage.isEmpty();
    }

    public static GetUserResponseDto getEmpty() {
        return GetUserResponseDto.builder()
                .name("")
                .loginId("")
                .email("")
                .profileImage("")
                .build();
    }
}
