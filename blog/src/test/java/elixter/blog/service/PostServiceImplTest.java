package elixter.blog.service;

import elixter.blog.domain.post.Post;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.service.post.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostServiceImplTest {
    @Autowired
    PostService postService;

    @Test
    void updatePost() {
        CreatePostRequestDto dto = CreatePostRequestDto.builder()
                .title("test")
                .category("test")
                .content("test")
                .thumbnail("test")
                .build();

        Post post = postService.createPost(dto);

        System.out.println("post = " + post);

        post.setContent("update test");
        postService.updatePost(post);

        GetPostResponseDto result = postService.findPostById(post.getId());

        GetPostResponseDto expect = new GetPostResponseDto();
        expect.postMapping(post);

        System.out.println("result = " + result);

        assertThat(expect).isEqualTo(result);
    }

    @Test
    void findAllPost() {
        CreatePostRequestDto dto = CreatePostRequestDto.builder()
                .title("test")
                .category("test")
                .content("test")
                .thumbnail("test")
                .hashtags(Arrays.asList("test", "hashtag"))
                .build();
        Post post = postService.createPost(dto);
        GetPostResponseDto responseDto1 = new GetPostResponseDto(post);

        CreatePostRequestDto dto2 = CreatePostRequestDto.builder()
                .title("test")
                .category("test_category")
                .content("test")
                .thumbnail("test")
                .build();
        Post post2 = postService.createPost(dto2);
        GetPostResponseDto responseDto2 = new GetPostResponseDto(post2);

        CreatePostRequestDto dto3 = CreatePostRequestDto.builder()
                .title("test")
                .category("test")
                .content("test")
                .thumbnail("test")
                .build();
        Post post3 = postService.createPost(dto3);
        GetPostResponseDto responseDto3 = new GetPostResponseDto(post3);

        CreatePostRequestDto dto4 = CreatePostRequestDto.builder()
                .title("test")
                .category("test_category")
                .content("test")
                .thumbnail("test")
                .build();
        Post post4 = postService.createPost(dto4);
        GetPostResponseDto responseDto4 = new GetPostResponseDto(post4);

        GetAllPostsResponseDto test_category = postService.findAllPost(PostService.FILTER_CATEGORY, "test_category", PageRequest.of(0, 20));
        System.out.println("test_category = " + test_category.getPosts());
        System.out.println("Arrays.asList(responseDto2, responseDto4) = " + Arrays.asList(responseDto2, responseDto4));

        Assertions.assertThat(test_category.getPosts()).contains(responseDto2, responseDto4);
        Assertions.assertThat(test_category.getPosts()).doesNotContain(responseDto1, responseDto3);
    }

    @Test
    void delete() {
        CreatePostRequestDto dto = CreatePostRequestDto.builder()
                .title("test")
                .category("test")
                .content("test")
                .thumbnail("test")
                .hashtags(Arrays.asList("test", "hashtag"))
                .build();
        Post post = postService.createPost(dto);
        postService.deletePost(post.getId());

        GetPostResponseDto postById = postService.findPostById(post.getId());
        Assertions.assertThat(postById.isEmpty()).isTrue();
    }
}
