package elixter.blog.service;

import elixter.blog.domain.auth.EmailVerify;
import elixter.blog.repository.auth.EmailVerifyRepository;
import elixter.blog.service.auth.CertificationService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class CertificationServiceTest {

    private final EmailVerifyRepository emailVerifyRepository;
    private final CertificationService service;

    @Autowired
    public CertificationServiceTest(
            @Qualifier("mockEmailCertificationService") CertificationService service,
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
        String certificationCode = service.generateEmailCertificationCode("ltw971@naver.com");

        Assertions.assertThat(service.validateEmailByCode("ltw971@naver.com", certificationCode)).isTrue();
    }
}
