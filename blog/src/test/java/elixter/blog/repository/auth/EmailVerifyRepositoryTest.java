package elixter.blog.repository.auth;

import elixter.blog.domain.auth.EmailVerify;
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
public class EmailVerifyRepositoryTest {

    private final EmailVerifyRepository emailVerifyRepository;
    private final RedisTemplate redisTemplate;

    @Autowired
    public EmailVerifyRepositoryTest(EmailVerifyRepository emailVerifyRepository, RedisTemplate redisTemplate) {
        this.emailVerifyRepository = emailVerifyRepository;
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
        EmailVerify emailVerify = new EmailVerify("ltw971@naver.com", "1q2w3e4r");

        emailVerify.setId(emailVerifyRepository.save(emailVerify).getId());
        log.info("emailCert={}", emailVerify);

        Assertions.assertThat(emailVerifyRepository.findByEmail("ltw971@naver.com").get()).isEqualTo(emailVerify);
        Assertions.assertThat(emailVerifyRepository.findByCode("1q2w3e4r").get()).isEqualTo(emailVerify);
    }
}
