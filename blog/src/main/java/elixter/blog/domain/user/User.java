package elixter.blog.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.tomcat.jni.Time;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class User {
    public static final String defaultProfileImage = "default";
    private Long id;
    private String name;
    private String loginId;
    private String loginPw;
    private String email;
    private String profileImage;
    private LocalDateTime createAt;

    public User() {
        createAt = LocalDateTime.now();
    }

    public User(Long id, String name, String loginPw, String email, String profileImage) {
        this.id = id;
        this.name = name;
        this.loginPw = loginPw;
        this.email = email;
        this.profileImage = profileImage;
    }

    public User(Long id, String name, String loginId, String loginPw, String email, String profileImage, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.email = email;
        this.profileImage = profileImage;
        this.createAt = createAt;
        this.createAt = LocalDateTime.now();
    }

    public User(String name, String loginId, String loginPw, String email) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.email = email;
        this.createAt = LocalDateTime.now();
    }

    public User(String loginPw, String email, String profileImage) {
        this.loginPw = loginPw;
        this.email = email;
        this.profileImage = profileImage;
    }

    public User (User user) {
        this.id = user.id;
        this.name = user.name;
        this.loginId = user.loginId;
        this.loginPw = user.loginPw;
        this.email = user.email;
        this.profileImage = user.profileImage;
        this.createAt = user.createAt;
        this.createAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = true;
        User _obj = (User)obj;

        if (obj instanceof User) {
            if (!this.id.equals(((User) obj).id) ||
                    !this.name.equals(((User) obj).name) ||
                    !this.loginId.equals(((User) obj).loginId) ||
                    !this.loginPw.equals(((User) obj).loginPw) ||
                    !this.profileImage.equals(((User) obj).profileImage)
            ) {
                result = false;
            }
        }
        else {
            result = false;
        }

        return result;
    }
}
