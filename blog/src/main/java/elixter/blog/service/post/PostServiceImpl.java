package elixter.blog.service.post;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import elixter.blog.domain.postImage.PostImage;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.dto.post.UpdatePostRequestDto;
import elixter.blog.repository.hashtag.HashtagRepository;
import elixter.blog.repository.hashtag.JdbcTemplateHashtagRepository;
import elixter.blog.repository.image.ImageRepository;
import elixter.blog.repository.post.JdbcTemplatePostRepository;
import elixter.blog.repository.post.PostRepository;
import elixter.blog.repository.postImage.PostImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Slf4j
@Component
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;
    private final PostImageRepository postImageRepository;

    private final String cacheName = "posts";


    private static String serverUri;

    @Autowired
    public PostServiceImpl(
            @Qualifier("jpaPostRepository") PostRepository postRepository,
            @Qualifier("jpaImageRepository") ImageRepository imageRepository,
            @Qualifier("jpaHashtagRepository") HashtagRepository hashtagRepository,
            PostImageRepository postImageRepository
    ) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.hashtagRepository = hashtagRepository;
        this.postImageRepository = postImageRepository;
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

        newPost = postRepository.save(newPost);

        List<Hashtag> hashtags = new ArrayList<>();
        for (String hashtag : post.getHashtags()) {
            hashtags.add(
                    Hashtag.builder()
                            .tag(hashtag)
                            .status(RecordStatus.exist)
                            .post(newPost)
                            .build()
            );
        }
        hashtagRepository.saveAll(hashtags);
        newPost.getHashtags().addAll(hashtags);

        if (post.getImageUrlList().isEmpty()) {
            return newPost;
        }

        List<String> storedNameList = new ArrayList<>();
        for (String imageUrl : post.getImageUrlList()) {
            storedNameList.add(StringUtils.removeStart(imageUrl, serverUri + "/api/images/"));
        }
        List<Image> images = imageRepository.findByStoredName(storedNameList);

        List<PostImage> postImages = new ArrayList<>();
        for (Image image : images) {
            postImages.add(PostImage.builder()
                    .post(newPost)
                    .image(image)
                    .build());
        }
        postImageRepository.saveAll(postImages);

        return newPost;
    }

    @Override
    @Transactional
    @CacheEvict(value = cacheName, allEntries = true)
    public void updatePost(UpdatePostRequestDto post) {

        Post updatePost = post.postMapping();
        updatePost.setUpdateAt(LocalDateTime.now().withNano(0));
        postRepository.update(updatePost);

        hashtagRepository.deleteByPostId(post.getId());
        List<Hashtag> hashtags = new ArrayList<>();
        for (String hashtag : post.getHashtags()) {
            hashtags.add(
                    Hashtag.builder()
                            .tag(hashtag)
                            .status(RecordStatus.exist)
                            .post(updatePost)
                            .build()
            );
        }
        hashtagRepository.saveAll(hashtags);
        updatePost.getHashtags().addAll(hashtags);

        postImageRepository.deleteByPostId(updatePost.getId());
        if (post.getImageUrlList().isEmpty()) {
            return;
        }

        List<String> storedNameList = new ArrayList<>();
        for (String imageUrl : post.getImageUrlList()) {
            storedNameList.add(StringUtils.removeStart(imageUrl, serverUri + "/api/images/"));
        }
        List<Image> images = imageRepository.findByStoredName(storedNameList);

        List<PostImage> postImages = new ArrayList<>();
        for (Image image : images) {
            postImages.add(PostImage.builder()
                    .post(updatePost)
                    .image(image)
                    .build());
        }
        postImageRepository.saveAll(postImages);
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
        GetAllPostsResponseDto result;

        switch (filter) {
            case FILTER_TITLE:
                log.info("not implemented : case FILTER_TITLE");
                postList = new ArrayList<>();
                result = new GetAllPostsResponseDto();
                break;
            case FILTER_CATEGORY:
                queryResult = postRepository.findByCategoryAndStatus(filterVal, RecordStatus.exist, pageable);
                postList = queryResult.getContent();
                result = new GetAllPostsResponseDto(queryResult.getPageable(), queryResult.getTotalElements(), queryResult.getTotalPages());
                break;
            case FILTER_CONTENT:
                log.info("not implemented : case FILTER_CONTENT");
                postList = new ArrayList<>();
                result = new GetAllPostsResponseDto();
                break;
            case FILTER_HASHTAG:
                queryResult = postRepository.findByHashtagAndStatus(filterVal, RecordStatus.exist, pageable);
                postList = queryResult.getContent();
                result = new GetAllPostsResponseDto(queryResult.getPageable(), queryResult.getTotalElements(), queryResult.getTotalPages());
                break;
            default:
                postList = new ArrayList<>();
                result = new GetAllPostsResponseDto();
                break;
        }

        for (Post post : postList) {
            if (hashtagRepository instanceof JdbcTemplateHashtagRepository) {
                List<Hashtag> hashtagList = hashtagRepository.findByPostId(post.getId());
                result.getPosts().add(new GetPostResponseDto(post, hashtagList));
            } else {
                result.getPosts().add(new GetPostResponseDto(post));
            }
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
//        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        executorService.submit(() -> {
//            log.debug("start relate image");
//
//            List<String> storedNameList = getActiveImageStoredNames(content, imageUrlList);
//            List<Image> images = imageRepository.findByStoredName(storedNameList);
//            images.forEach(image -> image.setPost(newPost));
//
//            imageRepository.saveAll(images);
//
//            log.debug("relate image is finished");
//        });
//        executorService.shutdown();
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

    private void setPostToHashtagList(Post newPost) {
        newPost.getHashtags().forEach(hashtag -> hashtag.setPost(newPost));
    }
}
