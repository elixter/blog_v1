package elixter.blog.exception.post;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class PostControllerAdvice {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Map<String, Object>> postNotFoundExceptionHandler(PostNotFoundException e) {
        Map<String, Object> response = e.getRestExceptionResponseMap();

        return new ResponseEntity<>(response, e.getStatus());
    }
}
