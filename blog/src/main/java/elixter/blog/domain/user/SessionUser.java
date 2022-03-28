package elixter.blog.domain.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.Serializable;

@Slf4j
@Getter
@Setter
@ToString
public class SessionUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String userLoginId;

    public SessionUser(Long userId, String userLoginId) {
        this.userId = userId;
        this.userLoginId = userLoginId;
    }

    @PostConstruct
    public void test() {
        log.debug("userId = {}", userId);
        log.debug("userLoginId = {}", userLoginId);
    }
}
