package elixter.blog.service.auth;

import elixter.blog.domain.auth.EmailCert;
import elixter.blog.exception.user.UserNotFoundException;
import elixter.blog.repository.auth.EmailCertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("mockEmailCertificationService")
public class MockEmailCertificationServiceImpl implements CertificationService {

    private final EmailCertRepository emailCertRepository;

    @Autowired
    public MockEmailCertificationServiceImpl(EmailCertRepository emailCertRepository) {
        this.emailCertRepository = emailCertRepository;
    }

    @Override
    public boolean ValidateEmailByCode(String email, String code) {
        EmailCert emailCert = emailCertRepository.findByEmail(email).orElseThrow();
        boolean certified = emailCert.getCode().equals(code);

        if (certified) {
            emailCertRepository.deleteById(emailCert.getId());
        }

        return certified;
    }
}
