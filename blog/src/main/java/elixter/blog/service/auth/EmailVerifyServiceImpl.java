package elixter.blog.service.auth;

import elixter.blog.domain.auth.EmailVerify;
import elixter.blog.repository.auth.EmailVerifyRepository;
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
public class EmailVerifyServiceImpl implements CertificationService {

    @Value("${emailCertExpire}")
    private Long expireTime;

    private final EmailVerifyRepository emailVerifyRepository;
    private final RedisTemplate redisTemplate;

    @Autowired
    public EmailVerifyServiceImpl(EmailVerifyRepository emailVerifyRepository, RedisTemplate redisTemplate) {
        this.emailVerifyRepository = emailVerifyRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean validateEmailByCode(String email, String code) {
        EmailVerify emailVerify = emailVerifyRepository.findByEmail(email).orElseThrow();
        boolean certified = emailVerify.getCode().equals(code);

        if (certified) {
            log.info("certification success with email={}", email);
            emailVerifyRepository.deleteById(emailVerify.getId());
        }

        return certified;
    }

    @Override
    public String generateEmailCertificationCode(String email) {

        // 알파뱃, 숫자조합 10자리 랜덤 코드 생성
        String certificationCode = RandomStringUtils.random(10, true, true);
        log.debug("generated certification code={}", certificationCode);

        EmailVerify emailVerify = emailVerifyRepository.save(new EmailVerify(email, certificationCode));
        redisTemplate.expire(emailVerify.getId(), expireTime, TimeUnit.MINUTES);

        return certificationCode;
    }
}
