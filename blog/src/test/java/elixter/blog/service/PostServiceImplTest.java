package elixter.blog.service;

import elixter.blog.domain.post.Post;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.dto.post.UpdatePostRequestDto;
import elixter.blog.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SpringBootTest
@Transactional
public class PostServiceImplTest {

    @Autowired
    PostService postService;

    @Test
    void createPost() {
        CreatePostRequestDto dto = CreatePostRequestDto.builder()
                .title("test")
                .category("test")
                .content("test")
                .thumbnail("test")
                .hashtags(Arrays.asList("test", "hashtag", "isGood"))
                .imageUrlList(Arrays.asList(
                        "http://localhost:8080/api/images/fd9429d3-0796-40d2-8104-0b39524de4bf.png",
                        "http://localhost:8080/api/images/69d50b14-75e1-47a6-b735-5b0626c06edf.png"
                ))
                .build();

        Post post = postService.createPost(dto);
        log.info("result = {}", post);
    }

    @Test
    void updatePost() {
        CreatePostRequestDto dto = CreatePostRequestDto.builder()
                .title("test")
                .category("test")
                .content("test")
                .thumbnail("test")
                .hashtags(Arrays.asList("test", "hashtag", "isGood"))
                .build();

        Post post = postService.createPost(dto);

        System.out.println("post = " + post);

        post.setContent("update test");
        List<String> hashList = new ArrayList<>();
        post.getHashtags().forEach(hashtag -> hashList.add(hashtag.getTag()));
        UpdatePostRequestDto updateDto = UpdatePostRequestDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .content("asdf")
                .imageUrlList(new ArrayList<>())
                .createAt(post.getCreateAt())
                .thumbnail(post.getThumbnail())
                .hashtags(hashList)
                .build();

        post.setContent("asdf");
        postService.updatePost(updateDto);

        GetPostResponseDto result = postService.findPostById(post.getId());

        GetPostResponseDto expect = new GetPostResponseDto();
        expect.postMapping(post);

        System.out.println("result = " + result);

        assertThat(expect).isEqualTo(result);

        assertThat(expect.getHashtags()).containsAll(hashList);
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
                .hashtags(new ArrayList<>())
                .build();
        Post post2 = postService.createPost(dto2);
        GetPostResponseDto responseDto2 = new GetPostResponseDto(post2);

        CreatePostRequestDto dto3 = CreatePostRequestDto.builder()
                .title("test")
                .category("test")
                .content("test")
                .thumbnail("test")
                .hashtags(new ArrayList<>())
                .build();
        Post post3 = postService.createPost(dto3);
        GetPostResponseDto responseDto3 = new GetPostResponseDto(post3);

        CreatePostRequestDto dto4 = CreatePostRequestDto.builder()
                .title("test")
                .category("test_category")
                .content("test")
                .thumbnail("test")
                .hashtags(new ArrayList<>())
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
