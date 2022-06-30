package elixter.blog.exception.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AuthControllerAdvice {


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> unauthorizedExceptionHandler(UnauthorizedException e) {
        Map<String, Object> response = e.getRestExceptionResponseMap();

        return new ResponseEntity<>(response, e.getStatus());
    }
}
