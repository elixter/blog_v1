package elixter.blog.message;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Message {
    private int statusCode;
    private String message;
    private Object data;

    public Message(HttpStatus statusCode, String message, Object data) {
        this.statusCode = statusCode.value();
        this.message = message;
        this.data = data;
    }
}
