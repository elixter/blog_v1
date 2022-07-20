package elixter.blog.service.email;

import javax.mail.MessagingException;
import java.util.List;

public interface EmailService {
    void send(String from, List<String> to, String title, String content) throws MessagingException;
}
