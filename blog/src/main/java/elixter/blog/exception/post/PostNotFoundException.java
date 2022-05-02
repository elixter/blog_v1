package elixter.blog.exception.post;

import elixter.blog.exception.RestException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostNotFoundException extends RestException {
    private final Object key;

    public PostNotFoundException(Object key) {
        super(HttpStatus.NOT_FOUND, "Data not found with Key : " + key);
        this.key = key;
    }
}
