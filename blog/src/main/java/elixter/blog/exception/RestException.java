package elixter.blog.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private HttpStatus status;
    private String message;

    public RestException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
