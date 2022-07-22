package elixter.blog.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import elixter.blog.constants.RecordStatus;
import lombok.*;
import org.apache.tomcat.jni.Time;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {
    public static final String defaultProfileImage = "default";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String loginId;

    private String loginPw;

    private String email;

    private String profileImage;

    private LocalDateTime createAt;

    private boolean emailVerified;

    private RecordStatus status;

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
    public User(
            Long id,
            String name,
            String loginId,
            String loginPw,
            String email,
            String profileImage,
            LocalDateTime createAt,
            Boolean emailVerified,
            RecordStatus status
    ) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.email = email;
        this.profileImage = profileImage;
        this.createAt = createAt;
        this.createAt = LocalDateTime.now().withNano(0);
        this.emailVerified = emailVerified;
        this.status = status;
    }

    public User(String name, String loginId, String loginPw, String email) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.email = email;
        this.createAt = LocalDateTime.now().withNano(0);
        this.status = RecordStatus.exist;
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
        this.emailVerified = user.emailVerified;
        this.status = user.status;
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
                .emailVerified(false)
                .build();
    }
}
