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
    private Long id;
    private String name;
    private String loginId;
    private String email;
    private String profileImage;

    public GetUserResponseDto() {
    }

    @Builder
    public GetUserResponseDto(Long id, String name, String loginId, String email, String profileImage) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.email = email;
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

    @JsonIgnore
    public boolean isEmpty() {
        return id.equals(-1L) &&
               name.isEmpty() &&
               loginId.isEmpty() &&
               email.isEmpty() &&
               profileImage.isEmpty();
    }

    public static GetUserResponseDto getEmpty() {
        return GetUserResponseDto.builder()
                .id(-1L)
                .name("")
                .loginId("")
                .email("")
                .profileImage("")
                .build();
    }
}
