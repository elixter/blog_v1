package elixter.blog.repository.hashtag;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import elixter.blog.domain.hashtag.HashtagCountInterface;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@SpringBootTest
class JpaHashtagRepositoryTest {

    private HashtagRepository hashtagRepository;

    @Autowired
    public JpaHashtagRepositoryTest(@Qualifier("jpaHashtagRepository") HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    @Test
    void searchTag() {
        List<HashtagCountInterface> result = hashtagRepository.searchTag("소통해요");

        result.forEach(res -> {
            System.out.println("res = " + res.getTag() + " " + res.getCount());
        });

    }

    @Test
    void saveBatch() {
        Post testPost = Post.builder().id(193L).status(RecordStatus.exist).build();

        Hashtag hashtag1 = Hashtag.builder().tag("ttt").post(testPost).status(RecordStatus.exist).build();
        Hashtag hashtag2 = Hashtag.builder().tag("ttt2").post(testPost).status(RecordStatus.exist).build();

        List<Hashtag> hashtagList = (List<Hashtag>) hashtagRepository.saveAll(Arrays.asList(
            hashtag1, hashtag2
        ));

        Assertions.assertThat(hashtagList).containsAll(Arrays.asList(hashtag1, hashtag2));
    }
}