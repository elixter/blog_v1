package elixter.blog.repository.hashtag;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.hashtag.HashtagCount;
import elixter.blog.domain.post.Post;
import elixter.blog.domain.hashtag.HashtagCountInterface;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@SpringBootTest
class HashtagRepositoryTest {

    private HashtagRepository hashtagRepository;

    @Autowired
    public HashtagRepositoryTest(@Qualifier("jdbcTemplateHashtagRepository") HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    @Test
    void saveAll() {
        Post testPost = Post.builder().id(193L).status(RecordStatus.exist).build();

        Hashtag hashtag1 = Hashtag.builder().tag("ttt").post(testPost).status(RecordStatus.exist).build();
        Hashtag hashtag2 = Hashtag.builder().tag("ttt2").post(testPost).status(RecordStatus.exist).build();

        List<Hashtag> hashtagList = (List<Hashtag>) hashtagRepository.saveAll(Arrays.asList(
            hashtag1, hashtag2
        ));

        Assertions.assertThat(hashtagList).containsAll(Arrays.asList(hashtag1, hashtag2));
    }

    @Test
    void searchTag() {
        Post testPost = Post.builder().id(193L).status(RecordStatus.exist).build();
        Hashtag hashtag1 = Hashtag.builder().tag("searchTest1").post(testPost).status(RecordStatus.exist).build();
        Hashtag hashtag2 = Hashtag.builder().tag("searchTest1").post(testPost).status(RecordStatus.exist).build();
        Hashtag hashtag3 = Hashtag.builder().tag("searchTest1").post(testPost).status(RecordStatus.exist).build();
        Hashtag hashtag4 = Hashtag.builder().tag("searchTest2").post(testPost).status(RecordStatus.exist).build();


        List<Hashtag> hashtagList = (List<Hashtag>) hashtagRepository.saveAll(Arrays.asList(
                hashtag1, hashtag2, hashtag3, hashtag4
        ));

        List<HashtagCount> result = new ArrayList<>();
        hashtagRepository.searchTag("searchTest").forEach(res -> {
            result.add(new HashtagCount(res.getTag(), res.getCount()));
        });

        Assertions.assertThat(result).containsAll(Arrays.asList(new HashtagCount("searchTest1", 3L), new HashtagCount("searchTest2", 1L)));
    }
}