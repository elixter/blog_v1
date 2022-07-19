package elixter.blog.domain.auth;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RedisHash("emailCert")
public class EmailCert implements Serializable {

    static private Long validateMinute = 3L;

    @Id
    private String id;

    @Indexed
    private String email;

    @Indexed
    private String code;

    private LocalDateTime expiredAt;

    public EmailCert() {
    }

    public EmailCert(String email) {
        this.email = email;
    }

    public EmailCert(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isBefore(expiredAt);
    }
}
