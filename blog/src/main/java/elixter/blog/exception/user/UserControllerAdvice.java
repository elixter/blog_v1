package elixter.blog.exception.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> userAlreadyExistExceptionhandler(UserAlreadyExistException e) {
        Map<String, Object> response = e.getRestExceptionResponseMap();

        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> userNotExistExceptionHandler(UserNotFoundException e) {
        Map<String, Object> response = e.getRestExceptionResponseMap();
        response.put("searchKey", e.getSearchKey());

        return new ResponseEntity<>(response, e.getStatus());
    }
}
