package elixter.blog.repository.auth;

import elixter.blog.domain.auth.EmailCert;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailCertRepository extends CrudRepository<EmailCert, String> {

    Optional<EmailCert> findByEmail(String email);
    Optional<EmailCert> findByCode(String code);
}
