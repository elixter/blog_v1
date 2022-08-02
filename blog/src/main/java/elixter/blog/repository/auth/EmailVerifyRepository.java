package elixter.blog.repository.auth;

import elixter.blog.domain.auth.EmailVerify;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailVerifyRepository extends CrudRepository<EmailVerify, String> {

    Optional<EmailVerify> findByEmail(String email);
    Optional<EmailVerify> findByCode(String code);
}
