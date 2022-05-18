package elixter.blog.exception.user;

import elixter.blog.exception.RestException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class UserNotFoundException extends RestException {

    private final Map<String, String> searchKey;

    public UserNotFoundException(Map<String, String> searchKey) {
        super(HttpStatus.NOT_FOUND, "user not found");
        this.searchKey = searchKey;
    }
}
