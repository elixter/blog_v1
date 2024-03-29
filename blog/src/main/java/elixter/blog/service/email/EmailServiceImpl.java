package elixter.blog.service.email;

import elixter.blog.domain.RecordStatus;
import elixter.blog.domain.mail.Mail;
import elixter.blog.repository.mail.MailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(MailRepository mailRepository,
                            JavaMailSender javaMailSender,
                            TemplateEngine templateEngine) {
        this.mailSender = javaMailSender;
        this.mailRepository = mailRepository;
        this.templateEngine = templateEngine;
    }

    @Override
    public void send(String sender, List<String> receivers, String title, String content) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;

        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(sender);
            messageHelper.setTo(receivers.toArray(new String[receivers.size()]));
            messageHelper.setSubject(title);
            messageHelper.setText(content);
        } catch (MessagingException e) {
            throw new RuntimeException("MessagingException from MailService send()");
        }

        mailSender.send(mimeMessage);

        log.info("mail={} sent successfully", mimeMessage);

        mailRepository.saveAll(
                receivers.stream()
                .map(receiver -> Mail.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .title(title)
                        .content(content)
                        .status(RecordStatus.exist)
                        .build())
                .collect(Collectors.toList())
        );
    }

    @Override
    public void sendTemplate(String sender, List<String> receivers, String title, String template, Map<String, Object> model) {

        String content = genTemplateMailContent(template, model);
        send(sender, receivers, title, content);
    }

    private String genTemplateMailContent(String template, Map<String, Object> model) {

        Context ctx = new Context();
        ctx.setVariables(model);
        return templateEngine.process(template, ctx);
    }
}
