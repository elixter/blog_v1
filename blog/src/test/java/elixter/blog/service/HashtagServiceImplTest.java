package elixter.blog.service;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import elixter.blog.repository.post.PostRepository;
import elixter.blog.service.hashtag.HashtagService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
class HashtagServiceImplTest {

    @Autowired
    HashtagService hashtagService;

    @Autowired
    PostRepository postRepository;

    Post post;

    @BeforeEach
    void beforeEach() {
        post = Post.builder()
                .category("test")
                .content("asdf")
                .thumbnail("asdfa")
                .title("tetst")
                .createAt(LocalDateTime.now().withNano(0))
                .updateAt(LocalDateTime.now().withNano(0))
                .status(RecordStatus.exist)
                .build();

        post = postRepository.save(post);
    }

    @Test
    void createHashtag() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.setPostId(post.getId());

        Number id = hashtagService.createHashtag(hashtag);

        Hashtag result = hashtagService.findHashtagById(id.longValue()).get();
        Assertions.assertThat(result).isEqualTo(hashtag);
    }

    @Test
    void createHashtags() {
        List<Hashtag> hashtagList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Hashtag hashtag = new Hashtag();
            hashtag.setTag("소통해요");
            hashtag.setPostId(post.getId());

            hashtagList.add(hashtag);
        }

        List<Long> idList = hashtagService.createHashtags(hashtagList);
        idList.forEach(id -> Assertions.assertThat(hashtagList).contains(hashtagService.findHashtagById(id).get()));
    }

    @Test
    void findHashtagById() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.setPostId(post.getId());

        Number id = hashtagService.createHashtag(hashtag);

        Hashtag result = hashtagService.findHashtagById(id.longValue()).get();
        Assertions.assertThat(result).isEqualTo(hashtag);

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> hashtagService.findHashtagById(id.longValue() + 999).get());
    }

    @Test
    void findAllHashtag() {
        List<Hashtag> hashtagList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Hashtag hashtag = new Hashtag();
            hashtag.setTag("소통해요");
            hashtag.setPostId(post.getId());

            hashtagList.add(hashtag);
        }

        hashtagService.createHashtags(hashtagList);
        List<Hashtag> result = hashtagService.findAllHashtag();

        hashtagList.forEach(hashtag -> Assertions.assertThat(result).contains(hashtag));
    }

    @Test
    void findHashtagByTag() {
        List<Hashtag> hashtagList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Hashtag hashtag = new Hashtag();
            hashtag.setTag("소통해요" + i);
            hashtag.setPostId(post.getId());

            hashtagList.add(hashtag);
        }

        hashtagService.createHashtags(hashtagList);
        List<Hashtag> result = hashtagService.findHashtagByTag("소통해요0");
        Assertions.assertThat(result).contains(hashtagList.get(0));

        List<Hashtag> result2 = hashtagService.findHashtagByTag("소통안해");
        Assertions.assertThat(result2.size()).isEqualTo(0);
    }

    @Test
    void findHashtagByPostId() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.setPostId(post.getId());

        Number id = hashtagService.createHashtag(hashtag);

        List<Hashtag> result = hashtagService.findHashtagByPostId(post.getId());
        Assertions.assertThat(result.size()).isEqualTo(1);

        List<Hashtag> result2 = hashtagService.findHashtagByPostId(300L);
        Assertions.assertThat(result2.size()).isEqualTo(0);
    }

    @Test
    void deleteHashtagById() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.setPostId(post.getId());

        Number id = hashtagService.createHashtag(hashtag);
        hashtagService.deleteHashtagById(id.longValue());

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> hashtagService.findHashtagById(id.longValue()).get());
    }

    @Test
    void deleteHashtagByTag() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.setPostId(post.getId());

        Number id = hashtagService.createHashtag(hashtag);
        hashtagService.deleteHashtagByTag("소통해요");

        List<Hashtag> result = hashtagService.findHashtagByTag("소통해요");
        Assertions.assertThat(result.size()).isEqualTo(0);
    }
}