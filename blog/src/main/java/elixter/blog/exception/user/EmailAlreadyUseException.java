package elixter.blog.exception.user;

import elixter.blog.exception.RestException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyUseException extends RestException {

    public EmailAlreadyUseException() {
        super(HttpStatus.CONFLICT, "Email already used");
    }
}
