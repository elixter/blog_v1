package elixter.blog.service.auth;

import elixter.blog.domain.auth.EmailCert;
import elixter.blog.repository.auth.EmailCertRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Qualifier("mockEmailCertificationService")
public class MockEmailCertificationServiceImpl implements CertificationService {

    @Value("${emailCertExpire}")
    private Long expireTime;

    private final EmailCertRepository emailCertRepository;
    private final RedisTemplate redisTemplate;

    @Autowired
    public MockEmailCertificationServiceImpl(EmailCertRepository emailCertRepository, RedisTemplate redisTemplate) {
        this.emailCertRepository = emailCertRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean validateEmailByCode(String email, String code) {
        EmailCert emailCert = emailCertRepository.findByEmail(email).orElseThrow();
        boolean certified = emailCert.getCode().equals(code);

        if (certified) {
            log.info("certification success with email={}", email);
            emailCertRepository.deleteById(emailCert.getId());
        }

        return certified;
    }

    @Override
    public String generateEmailCertificationCode(String email) {

        // 알파뱃, 숫자조합 10자리 랜덤 코드 생성
        String certificationCode = RandomStringUtils.random(10, true, true);
        log.debug("generated certification code={}", certificationCode);

        EmailCert emailCert = emailCertRepository.save(new EmailCert(email, certificationCode));
        redisTemplate.expire(emailCert.getId(), expireTime, TimeUnit.MINUTES);

        return certificationCode;
    }
}
