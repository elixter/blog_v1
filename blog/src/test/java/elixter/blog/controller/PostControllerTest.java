package elixter.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import elixter.blog.domain.post.Post;
import elixter.blog.dto.PageDto;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.service.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostService postService;

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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

    @Test
    @Transactional
    void getPostWithId() throws Exception {

        CreatePostRequestDto requestBody = CreatePostRequestDto.builder()
                .title("for testing")
                .content("teeeeeeeeesting")
                .thumbnail("http://testingImage")
                .category("category")
                .imageUrlList(Arrays.asList("http://testingImage", "http://testingImage2"))
                .hashtags(Arrays.asList("소통해요", "허허허"))
                .build();
        Post post = postService.createPost(requestBody);
        GetPostResponseDto expect = new GetPostResponseDto(post, post.getHashtags());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts/" + post.getId())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expect)));
    }

    @Test
    @Transactional
    void findPostsByCategory() throws Exception {

        CreatePostRequestDto requestBody1 = CreatePostRequestDto.builder()
                .title("for testing")
                .content("teeeeeeeeesting")
                .thumbnail("http://testingImage")
                .category("TestCategory1")
                .imageUrlList(Arrays.asList("http://testingImage", "http://testingImage2"))
                .hashtags(Arrays.asList("소통해요", "허허허"))
                .build();
        Post post1 = postService.createPost(requestBody1);
        GetPostResponseDto expectData1 = new GetPostResponseDto(post1, post1.getHashtags());
        Thread.sleep(1000);

        CreatePostRequestDto requestBody2 = CreatePostRequestDto.builder()
                .title("for testing232323")
                .content("테ㅔㅔㅔㅔㅔ스팅")
                .thumbnail("http://testingImage")
                .category("TestCategory1")
                .imageUrlList(Arrays.asList("http://testingImage", "http://testingImage2"))
                .hashtags(Arrays.asList("소통해요", "테스트2"))
                .build();
        Post post2 = postService.createPost(requestBody2);
        GetPostResponseDto expectData2 = new GetPostResponseDto(post2, post2.getHashtags());
        Thread.sleep(1000);

        CreatePostRequestDto requestBody3 = CreatePostRequestDto.builder()
                .title("for testing232323")
                .content("teeeeeeeeesting232323232323")
                .thumbnail("http://testingImage")
                .category("TestCategory2")
                .imageUrlList(Arrays.asList("http://testingImage", "http://testingImage2"))
                .hashtags(Arrays.asList("소통해요", "테스트3"))
                .build();
        Post post3 = postService.createPost(requestBody3);
        GetPostResponseDto expectData3 = new GetPostResponseDto(post3, post3.getHashtags());
        Thread.sleep(1000);

        GetAllPostsResponseDto expectResponse = new GetAllPostsResponseDto();
        expectResponse.setPosts(Arrays.asList(expectData2, expectData1));
        expectResponse.setPage(new PageDto(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createAt")),2L, 1));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts?filterType=category&filterString=TestCategory1")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectResponse)));

        GetAllPostsResponseDto expectResponse2 = new GetAllPostsResponseDto();
        expectResponse2.setPosts(Arrays.asList(expectData3));
        expectResponse2.setPage(new PageDto(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createAt")),1L, 1));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts?filterType=category&filterString=TestCategory2")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectResponse2)));

    }

    @Test
    @Transactional
    void findPostsByHashtag() throws Exception {

        CreatePostRequestDto requestBody1 = CreatePostRequestDto.builder()
                .title("for testing")
                .content("teeeeeeeeesting")
                .thumbnail("http://testingImage")
                .category("TestCategory1")
                .imageUrlList(Arrays.asList("http://testingImage", "http://testingImage2"))
                .hashtags(Arrays.asList("소통해요테스트", "소통해요테스트허허허"))
                .build();
        Post post1 = postService.createPost(requestBody1);
        GetPostResponseDto expectData1 = new GetPostResponseDto(post1, post1.getHashtags());

        CreatePostRequestDto requestBody2 = CreatePostRequestDto.builder()
                .title("for testing232323")
                .content("테ㅔㅔㅔㅔㅔ스팅")
                .thumbnail("http://testingImage")
                .category("TestCategory1")
                .imageUrlList(Arrays.asList("http://testingImage", "http://testingImage2"))
                .hashtags(Arrays.asList("소통해요테스트", "소통해요테스트헤헤"))
                .build();
        Post post2 = postService.createPost(requestBody2);
        GetPostResponseDto expectData2 = new GetPostResponseDto(post2, post2.getHashtags());

        CreatePostRequestDto requestBody3 = CreatePostRequestDto.builder()
                .title("for testing232323")
                .content("teeeeeeeeesting232323232323")
                .thumbnail("http://testingImage")
                .category("TestCategory2")
                .imageUrlList(Arrays.asList("http://testingImage", "http://testingImage2"))
                .hashtags(Arrays.asList("소통해요테스트"))
                .build();
        Post post3 = postService.createPost(requestBody3);
        GetPostResponseDto expectData3 = new GetPostResponseDto(post3, post3.getHashtags());

        GetAllPostsResponseDto expectResponse = new GetAllPostsResponseDto();
        expectResponse.setPosts(Arrays.asList(expectData1, expectData2, expectData3));
        expectResponse.setPage(new PageDto(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createAt")),3L, 1));


        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts?filterType=hashtag&filterString=소통해요테스트")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectResponse)));

        GetAllPostsResponseDto expectResponse2 = new GetAllPostsResponseDto();
        expectResponse2.setPosts(Arrays.asList(expectData2));
        expectResponse2.setPage(new PageDto(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createAt")),1L, 1));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts?filterType=hashtag&filterString=소통해요테스트헤헤")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectResponse2)));

    }
}
