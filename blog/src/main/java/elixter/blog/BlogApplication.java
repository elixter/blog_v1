package elixter.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableCaching
@SpringBootApplication
public class BlogApplication {
	// TODO: RecordStatus Ordinal이 아닌 String으로 바꿔야함

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}
