package elixter.blog.dto.user;

import elixter.blog.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserResponseDto {
    private Long id;
    private String name;
    private String loginId;
    private String email;
    private String profileImage;

    public GetUserResponseDto() {
    }

    @Builder
    public GetUserResponseDto(Long id, String name, String loginId, String profileImage) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.profileImage = profileImage;
    }

    public GetUserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
    }

    public void mapping(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
    }
}
