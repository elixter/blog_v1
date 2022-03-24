package elixter.blog.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String, Object>> handler(RestException e) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", e.getTimestamp());
        response.put("status", e.getStatus().value());
        response.put("message", e.getMessage());

        return new ResponseEntity<>(response, e.getStatus());
    }
}
