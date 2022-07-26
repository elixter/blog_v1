package elixter.blog.service;

import elixter.blog.domain.auth.EmailVerify;
import elixter.blog.repository.auth.EmailVerifyRepository;
import elixter.blog.service.user.VerifyService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class VerifyServiceTest {

    private final EmailVerifyRepository emailVerifyRepository;
    private final VerifyService service;

    @Autowired
    public VerifyServiceTest(
            @Qualifier("mockEmailCertificationService") VerifyService service,
            EmailVerifyRepository emailVerifyRepository
    ) {
        this.service = service;
        this.emailVerifyRepository = emailVerifyRepository;
    }

    @Test
    void validateCode() {
        EmailVerify emailVerify = new EmailVerify("ltw971@naver.com", "1q2w3e4r");
        emailVerifyRepository.save(emailVerify);

        Assertions.assertThat(service.validateEmailByCode("ltw971@naver.com", "asdf123")).isFalse();
        Assertions.assertThat(service.validateEmailByCode("ltw971@naver.com", "1q2w3e4r")).isTrue();
    }

    @Test
    void generateEmailCertifyingCode() {
        String certificationCode = service.generateEmailVerificationCode("ltw971@naver.com");

        Assertions.assertThat(service.validateEmailByCode("ltw971@naver.com", certificationCode)).isTrue();
    }
}
