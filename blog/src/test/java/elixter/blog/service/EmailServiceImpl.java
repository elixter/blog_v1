package elixter.blog.service;

import elixter.blog.service.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.SendFailedException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class EmailServiceImpl {

    private final EmailService emailService;

    @Autowired
    public EmailServiceImpl(EmailService emailService) {
        this.emailService = emailService;
        log.info("EmailService={}", emailService);
    }

    @Test
    void send() {
        String from = "ltw971@naver.com";
        List<String> to = Arrays.asList("ltw971@naver.com");
        String title = "test";
        String content = "test";

        Assertions.assertDoesNotThrow(() -> emailService.send(from, to, title, content));
    }

    @Test
    void send_badAddress() {
        String from = "sdasdasd";
        List<String> to = Arrays.asList("q12w2e");
        String title = "test";
        String content = "test";

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> emailService.send(from, to, title, content))
                .isInstanceOf(SendFailedException.class);
    }
}

