package elixter.blog.service.post;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.repository.hashtag.HashtagRepository;
import elixter.blog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public Long createPost(Post post) {
        postRepository.save(post);

        return post.getId();
    }

    @Override
    public void updatePost(Post post) {
        post.setUpdateAt(LocalDateTime.now());
        postRepository.update(post);
    }

    @Override
    public Optional<Post> findPostById(Long id) {
        Optional<Post> result = postRepository.findById(id);
        System.out.println("reuslt = " + result);

        return result;
    }

    @Override
    public GetAllPostsResponseDto findAllPost(String filter, String filterVal, Pageable pageable) {
        List<Post> postList;
        switch(filter) {
            case FILTER_TITLE:
                log.info("not implemented : case FILTER_TITLE");
                postList = new ArrayList<>();
                break;
            case FILTER_CATEGORY:
                postList = postRepository.findByCategory(filterVal, pageable.getOffset(), (long) pageable.getPageSize());
                break;
            case FILTER_CONTENT:
                log.info("not implemented : case FILTER_CONTENT");
                postList = new ArrayList<>();
                break;
            case FILTER_HASHTAG:
                postList = postRepository.findByHashtag(filterVal, pageable.getOffset(), (long) pageable.getPageSize());
                break;
            default:
                postList = new ArrayList<>();
                break;
        }

        GetAllPostsResponseDto result = new GetAllPostsResponseDto();

        for (Post post : postList) {
            List<Hashtag> hashtagList = hashtagRepository.findByPostId(post.getId());
            result.getPosts().add(new GetPostResponseDto(post, hashtagList));
        }

        return result;
    }

    @Override
    public List<Post> findPost(Long page, Long pageSize) {
        return postRepository.findAll(page, pageSize);
    }

    @Override
    public List<Post> findPostByCategory(String category, Long page, Long pageSize) {
        return postRepository.findByCategory(category, page, pageSize);
    }

    @Override
    public List<Post> findPostByHashtag(String hashtag, Long page, Long pageSize) {
        return postRepository.findByHashtag(hashtag, page, pageSize);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.delete(id);
    }
}
