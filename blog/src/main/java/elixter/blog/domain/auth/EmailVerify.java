package elixter.blog.domain.auth;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RedisHash("emailCert")
public class EmailVerify implements Serializable {

    static private Long validateMinute = 3L;

    @Id
    private String id;

    @Indexed // Redis key index
    private String email;

    @Indexed
    private String code;

    private LocalDateTime expiredAt;

    public EmailVerify() {
    }

    public EmailVerify(String email) {
        this.email = email;
    }

    public EmailVerify(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isBefore(expiredAt);
    }
}
