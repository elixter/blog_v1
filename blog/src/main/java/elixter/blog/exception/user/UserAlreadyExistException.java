package elixter.blog.exception.user;

import elixter.blog.exception.RestException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class UserAlreadyExistException extends RestException {

    public UserAlreadyExistException() {
        super(HttpStatus.CONFLICT, "email or login id is already used");
    }
}
