package elixter.blog.service.post;

import elixter.blog.domain.post.Post;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface PostService {
    public static final String FILTER_CATEGORY = "category";
    public static final String FILTER_HASHTAG = "HASHTAG";
    public static final String FILTER_CONTENT = "content";
    public static final String FILTER_TITLE = "title";
    public static final List<String> filterList = Arrays.asList(
            FILTER_CATEGORY,
            FILTER_HASHTAG,
            FILTER_CONTENT,
            FILTER_TITLE
    );

    Post createPost(CreatePostRequestDto post);

    void updatePost(Post post);

    GetPostResponseDto findPostById(Long id);

    GetAllPostsResponseDto findAllPost(String filter, String filterVal, Pageable pageable);

    List<Post> findPost(Long offset, Long limit);

    List<Post> findPostByCategory(String category, Long offset, Long limit);

    List<Post> findPostByHashtag(String hashtag, Long offset, Long limit);

    void deletePost(Long id);
}
