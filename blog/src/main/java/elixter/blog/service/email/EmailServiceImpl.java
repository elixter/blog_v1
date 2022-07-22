package elixter.blog.service.email;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.mail.Mail;
import elixter.blog.exception.RestException;
import elixter.blog.repository.mail.MailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;

    @Autowired
    public EmailServiceImpl(MailRepository mailRepository) {
        this.mailSender = new JavaMailSenderImpl();
        this.mailRepository = mailRepository;
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
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "MessagingException");
        }

        try {
            mailSender.send(mimeMessage);
        } catch (MailAuthenticationException e) {
            log.info("MailAuthenticationException", e);
            throw new RestException(HttpStatus.UNAUTHORIZED, "Mail Authentication needed");
        } catch (MailSendException e) {
            log.info("MailSendException", e);
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send mail");
        }

        log.info("mail={} sent successfully", mimeMessage);
        List<Mail> mailList = new ArrayList<>();
        for (String receiver : receivers) {
            mailList.add(Mail.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .title(title)
                    .content(content)
                    .status(RecordStatus.exist)
                    .build());
        }
        mailRepository.saveAll(mailList);
    }
}
