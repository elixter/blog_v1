package elixter.blog.service;

import elixter.blog.domain.auth.EmailCert;
import elixter.blog.repository.auth.EmailCertRepository;
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

    private final EmailCertRepository emailCertRepository;
    private final CertificationService service;

    @Autowired
    public CertificationServiceTest(
            @Qualifier("mockEmailCertificationService") CertificationService service,
            EmailCertRepository emailCertRepository
    ) {
        this.service = service;
        this.emailCertRepository = emailCertRepository;
    }

    @Test
    void validateCode() {
        EmailCert emailCert = new EmailCert("ltw971@naver.com", "1q2w3e4r");
        emailCertRepository.save(emailCert);

        Assertions.assertThat(service.ValidateEmailByCode("ltw971@naver.com", "1q2w3e4r")).isTrue();
        Assertions.assertThat(service.ValidateEmailByCode("ltw971@naver.com", "asdf123")).isFalse();

    }
}
