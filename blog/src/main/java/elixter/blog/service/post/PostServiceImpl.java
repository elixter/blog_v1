package elixter.blog.service.post;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.dto.post.UpdatePostRequestDto;
import elixter.blog.repository.hashtag.HashtagRepository;
import elixter.blog.repository.hashtag.JdbcTemplateHashtagRepository;
import elixter.blog.repository.image.ImageRepository;
import elixter.blog.repository.post.JdbcTemplatePostRepository;
import elixter.blog.repository.post.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;

    private final String cacheName = "posts";


    private static String serverUri;

    @Autowired
    public PostServiceImpl(@Qualifier("jpaPostRepository") PostRepository postRepository, ImageRepository imageRepository, @Qualifier("jpaHashtagRepository") HashtagRepository hashtagRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.hashtagRepository = hashtagRepository;
    }

    @Value("${server.uri}")
    private void setServerUri(String uri) {
        serverUri = uri;
    }

    @Override
    @Transactional
    @CacheEvict(value = cacheName, allEntries = true)
    public Post createPost(CreatePostRequestDto post) {
        Post newPost = post.postMapping();

        postRepository.save(newPost);
        setPostIdToHashtagList(newPost);
        hashtagRepository.saveAll(newPost.getHashtags());

        asyncRelateImageWithPost(newPost, newPost.getContent(), post.getImageUrlList());

        return newPost;
    }

    @Override
    @Transactional
    @CacheEvict(value = cacheName, allEntries = true)
    public void updatePost(UpdatePostRequestDto post) {
        Post updatePost = post.postMapping();
        updatePost.setUpdateAt(LocalDateTime.now().withNano(0));
        hashtagRepository.deleteByPostId(post.getId());

        setPostIdToHashtagList(updatePost);
        postRepository.update(updatePost);

        if (hashtagRepository instanceof JdbcTemplateHashtagRepository
            && postRepository instanceof JdbcTemplatePostRepository) {
            hashtagRepository.saveAll(updatePost.getHashtags());
        }

        asyncRelateImageWithPost(updatePost, post.getContent(), post.getImageUrlList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = cacheName, key = "#id")
    public GetPostResponseDto findPostById(Long id) {
        Post foundPost = postRepository.findByIdAndStatus(id, RecordStatus.exist).orElse(Post.emptyPost());
        if (foundPost.isEmpty()) {
            log.info("No such post with id [{}]", id);
            return GetPostResponseDto.getEmpty();
        }
        List<Hashtag> hashtagList = hashtagRepository.findByPostId(foundPost.getId());

        return new GetPostResponseDto(foundPost, hashtagList);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = cacheName, key = "{#filter, #filterVal, #pageable}")
    public GetAllPostsResponseDto findAllPost(String filter, String filterVal, Pageable pageable) {
        Page<Post> queryResult;
        List<Post> postList;
        switch (filter) {
            case FILTER_TITLE:
                log.info("not implemented : case FILTER_TITLE");
                postList = new ArrayList<>();
                break;
            case FILTER_CATEGORY:
                queryResult = postRepository.findByCategoryAndStatus(filterVal, RecordStatus.exist, pageable);
                postList = queryResult.getContent();
                break;
            case FILTER_CONTENT:
                log.info("not implemented : case FILTER_CONTENT");
                postList = new ArrayList<>();
                break;
            case FILTER_HASHTAG:
                queryResult = postRepository.findByHashtagAndStatus(filterVal, RecordStatus.exist, pageable);
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
    @Transactional
    @CacheEvict(value = cacheName, allEntries = true)
    public void deletePost(Long id) {
        postRepository.delete(id);
        hashtagRepository.deleteByPostId(id);
    }

    private void asyncRelateImageWithPost(Post newPost, String content, List<String> imageUrlList) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executorService.submit(() -> {
            log.debug("start relate image");

            List<String> storedNameList = getActiveImageStoredNames(content, imageUrlList);
            List<Image> images = imageRepository.findByStoredName(storedNameList);
            images.forEach(image -> image.setPost(newPost));

            imageRepository.saveAll(images);

            log.debug("relate image is finished");
        });
        executorService.shutdown();
    }

    private static List<String> getActiveImageStoredNames(String content, List<String> imageUrlList) {
        List<String> storedNameList = new Vector<>();

        imageUrlList.parallelStream().forEach(url -> {
            if (content.contains(url)) {
                storedNameList.add(url.replace(serverUri + "/api/image/", ""));
            }
        });

        return storedNameList;
    }

    private void setPostIdToHashtagList(Post newPost) {
        newPost.getHashtags().forEach(hashtag -> hashtag.getPost().setId(newPost.getId()));
    }
}
