package elixter.blog.repository.auth;

import elixter.blog.domain.auth.EmailCert;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@Slf4j
@SpringBootTest
public class EmailCertRepositoryTest {

    private final EmailCertRepository emailCertRepository;
    private final RedisTemplate redisTemplate;

    @Autowired
    public EmailCertRepositoryTest(EmailCertRepository emailCertRepository, RedisTemplate redisTemplate) {
        this.emailCertRepository = emailCertRepository;
        this.redisTemplate = redisTemplate;
    }

    @AfterEach
    void flushAll() {
        log.info("evict all emailCert data");
        String keyString = "emailCert:*";

        Set keys = redisTemplate.keys(keyString);
        List<String> keyList = new ArrayList<>();

        if (keys != null) {
            for (Object key : keys) {
                log.info("key = {}", key);
                keyList.add((String) key);
            }
        }

        log.info("found keys={}", keys);
        Long delete = redisTemplate.delete(keyList);
        log.info("emailCert {} records deleted", delete);
    }

    @Test
    void findTest() {
        EmailCert emailCert = new EmailCert("ltw971@naver.com", "1q2w3e4r");

        emailCert.setId(emailCertRepository.save(emailCert).getId());
        log.info("emailCert={}", emailCert);

        Assertions.assertThat(emailCertRepository.findByEmail("ltw971@naver.com").get()).isEqualTo(emailCert);
        Assertions.assertThat(emailCertRepository.findByCode("1q2w3e4r").get()).isEqualTo(emailCert);
    }
}
