package elixter.blog.exception.auth;

import elixter.blog.exception.RestException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedException extends RestException {

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "authentication needed.");
    }
}
