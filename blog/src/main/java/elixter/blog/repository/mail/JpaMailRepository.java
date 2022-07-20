package elixter.blog.repository.mail;

import elixter.blog.domain.mail.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaMailRepository implements MailRepository {

    private final SpringDataJpaMailRepository repository;

    @Autowired
    public JpaMailRepository(SpringDataJpaMailRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mail save(Mail mail) {
        return repository.save(mail);
    }

    @Override
    public List<Mail> saveAll(List<Mail> mail) {
        return repository.saveAll(mail);
    }

    @Override
    public Optional<Mail> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Mail> findAllBySender(String sender) {
        return repository.findAllBySender(sender);
    }

    @Override
    public List<Mail> findAllByReceiver(String receiver) {
        return repository.findAllByReceiver(receiver);
    }
}
