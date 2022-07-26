package elixter.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableCaching
@SpringBootApplication
public class BlogApplication {

	// TODO : 회원가입시 로그인 아이디 중복 확인 API
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}
