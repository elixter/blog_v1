package elixter.blog.service.post;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.repository.hashtag.HashtagRepository;
import elixter.blog.repository.image.ImageRepository;
import elixter.blog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public Post createPost(CreatePostRequestDto post) {
        Post newPost = post.postMapping();

        postRepository.save(newPost);
        setPostIdToHashtagList(newPost);
        hashtagRepository.batchSave(newPost.getHashtags());

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executorService.submit(() -> {
            List<String> storedNameList = getActiveImageUrls(newPost.getContent(), post.getImageUrlList());
            List<Image> images = imageRepository.findByStoredName(storedNameList);
            List<Long> imageIdList = new Vector<>();

            images.parallelStream().forEach(image -> imageIdList.add(image.getId()));
            try {
                imageRepository.relateWithPost(imageIdList, newPost.getId());
            } catch (DataIntegrityViolationException e) {
                log.error("relation with post {} failed", newPost.getId(), e);
            }
        });
        executorService.shutdown();

        return newPost;
    }

    @Override
    public void updatePost(Post post) {
        post.setUpdateAt(LocalDateTime.now().withNano(0));
        postRepository.update(post);

        hashtagRepository.deleteByPostId(post.getId());
        hashtagRepository.batchSave(post.getHashtags());
    }

    @Override
    public GetPostResponseDto findPostById(Long id) {
        Post foundPost = postRepository.findById(id).orElse(Post.emptyPost());
        if (foundPost.isEmpty()) {
            log.info("No such post with id [{}]", id);
            return GetPostResponseDto.getEmpty();
        }
        List<Hashtag> hashtagList = hashtagRepository.findByPostId(foundPost.getId());

        return new GetPostResponseDto(foundPost, hashtagList);
    }

    @Override
    public GetAllPostsResponseDto findAllPost(String filter, String filterVal, Pageable pageable) {
        Page<Post> queryResult;
        List<Post> postList;
        switch(filter) {
            case FILTER_TITLE:
                log.info("not implemented : case FILTER_TITLE");
                postList = new ArrayList<>();
                break;
            case FILTER_CATEGORY:
                queryResult = postRepository.findByCategory(filterVal, pageable);
                postList = queryResult.getContent();
                break;
            case FILTER_CONTENT:
                log.info("not implemented : case FILTER_CONTENT");
                postList = new ArrayList<>();
                break;
            case FILTER_HASHTAG:
                queryResult = postRepository.findByHashtag(filterVal, pageable);
                postList = queryResult.getContent();
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
    public void deletePost(Long id) {
        postRepository.delete(id);
        hashtagRepository.deleteByPostId(id);
    }

    private static List<String> getActiveImageUrls(String content, List<String> imageUrlList) {
        List<String> urlList = new Vector<>();

        imageUrlList.parallelStream().forEach(url -> {
            if (content.contains(url)) {
                urlList.add(url);
            }
        });

        return urlList;
    }

    private void setPostIdToHashtagList(Post newPost) {
        newPost.getHashtags().forEach(hashtag->hashtag.setPostId(newPost.getId()));
    }
}
