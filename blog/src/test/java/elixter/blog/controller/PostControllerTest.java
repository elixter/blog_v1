package elixter.blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.service.post.PostService;
import elixter.blog.service.post.PostServiceImpl;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Transactional
    void createPost() throws Exception {
        CreatePostRequestDto requestBody = CreatePostRequestDto.builder()
                .title("for testing")
                .content("teeeeeeeeesting")
                .thumbnail("http://testingImage")
                .category("category")
                .imageUrlList(Arrays.asList("http://testingImage", "http://testingImage2"))
                .hashtags(Arrays.asList("소통해요", "허허허"))
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        CreatePostRequestDto invalidFieldRequestBody = CreatePostRequestDto.builder()
                .title("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijasadasdasdasdasdasdasdasdbcdefghijabcdefghij") // title length must be equal or less than 50
                .content("teeeeeeeeesting")
                .thumbnail("http://testingImage")
                .category("") // category must not blank
                .imageUrlList(Arrays.asList("http://testingImage", "http://testingImage2"))
                .hashtags(Arrays.asList("소통해요", "허허허"))
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFieldRequestBody))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
