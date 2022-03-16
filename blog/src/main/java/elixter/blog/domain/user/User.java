package elixter.blog.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class User {
    private Long id;
    private String name;
    private String loginId;
    private String loginPw;
    private String profileImage;

    public User() {
    }

    public User(Long id, String name, String loginId, String loginPw, String profileImage) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.profileImage = profileImage;
    }
}
