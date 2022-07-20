package elixter.blog.repository.mail;

import elixter.blog.domain.mail.Mail;

import java.util.List;
import java.util.Optional;

public interface MailRepository {
    Mail save(Mail mail);
    List<Mail> saveAll(List<Mail> mail);
    Optional<Mail> findById(Long id);
    List<Mail> findAllBySender(String sender);
    List<Mail> findAllByReceiver(String receiver);
}
