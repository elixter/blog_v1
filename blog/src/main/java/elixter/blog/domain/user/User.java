package elixter.blog.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.tomcat.jni.Time;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
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
        createAt = LocalDateTime.now().withNano(0);
    }

    public User(Long id, String name, String loginPw, String email, String profileImage) {
        this.id = id;
        this.name = name;
        this.loginPw = loginPw;
        this.email = email;
        this.profileImage = profileImage;
    }

    @Builder
    public User(Long id, String name, String loginId, String loginPw, String email, String profileImage, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.email = email;
        this.profileImage = profileImage;
        this.createAt = createAt;
        this.createAt = LocalDateTime.now().withNano(0);
    }

    public User(String name, String loginId, String loginPw, String email) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.email = email;
        this.createAt = LocalDateTime.now().withNano(0);
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
        this.createAt = LocalDateTime.now().withNano(0);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return name.isEmpty() && loginId.isEmpty() && loginPw.isEmpty();
    }

    public static User getEmpty() {
        return User.builder()
                .name("")
                .loginId("")
                .loginPw("")
                .build();
    }
}
