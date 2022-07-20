package elixter.blog.repository.mail;

import elixter.blog.domain.mail.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaMailRepository extends JpaRepository<Mail, Long> {
    List<Mail> findAllBySender(String sender);
    List<Mail> findAllByReceiver(String sender);
}
