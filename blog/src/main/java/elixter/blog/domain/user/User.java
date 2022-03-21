package elixter.blog.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Time;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
public class User {
    private Long id;
    private String name;
    private String loginId;
    private String loginPw;
    private String profileImage;
    private LocalDateTime createAt;

    public User() {
        createAt = LocalDateTime.now();
    }

    public User(Long id, String name, String loginId, String loginPw, String profileImage, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.profileImage = profileImage;
        this.createAt = createAt;
    }

    public User (User user) {
        this.id = user.id;
        this.name = user.name;
        this.loginId = user.loginId;
        this.loginPw = user.loginPw;
        this.profileImage = user.profileImage;
        this.createAt = user.createAt;
    }
}
