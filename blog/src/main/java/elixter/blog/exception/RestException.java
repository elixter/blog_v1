package elixter.blog.exception;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;

    public RestException(HttpStatus status, String message) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }

    @NotNull
    public Map<String, Object> getRestExceptionResponseMap() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", timestamp);
        response.put("status", status.value());
        response.put("message", message);
        return response;
    }
}
