package elixter.blog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class InvalidBodyFieldException extends RestException {

    private Map<String, Object> fieldErrorsMap;

    public InvalidBodyFieldException(List<FieldError> fieldErrors) {
        super(HttpStatus.BAD_REQUEST, "Invalid request body field value");
        fieldErrorsMap = new HashMap<>();

        fieldErrors.forEach(
                fieldError -> fieldErrorsMap.put(fieldError.getField(), fieldError.getRejectedValue())
        );
    }
}
