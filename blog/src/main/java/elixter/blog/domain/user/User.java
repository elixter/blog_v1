package elixter.blog.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import elixter.blog.domain.RecordStatus;
import lombok.*;

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

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String loginId;

    @Column(length = 72, nullable = false)
    private String loginPw;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    @Column(nullable = false)
    private LocalDateTime createAt;

    private boolean emailVerified;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
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
