package elixter.blog.service.email;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface EmailService {
    void send(String from, List<String> to, String title, String content);
    void sendTemplate(String from, List<String> to, String title, String template, Map<String, Object> model);
}
