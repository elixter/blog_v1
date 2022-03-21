package elixter.blog.dto.user;

import elixter.blog.domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserResponseDto {
    private Long id;
    private String name;
    private String loginId;
    private String profileImage;

    public GetUserResponseDto() {
    }

    public GetUserResponseDto(Long id, String name, String loginId, String profileImage) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.profileImage = profileImage;
    }

    public void Mapping(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.loginId = user.getLoginId();
        this.profileImage = user.getProfileImage();
    }
}