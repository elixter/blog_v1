package elixter.blog.domain.auth;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@RedisHash("emailCert")
public class EmailCert implements Serializable {

    @Id
    private String id;

    @Indexed
    private String email;

    @Indexed
    private String code;

    public EmailCert() {
    }

    public EmailCert(String email) {
        this.email = email;
    }

    public EmailCert(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
