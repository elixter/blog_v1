package elixter.blog.repository.hashtag;

import elixter.blog.dto.hashtag.SearchHashtag;
import elixter.blog.dto.hashtag.SearchHashtagInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class JpaHashtagRepositoryTest {

    private HashtagRepository hashtagRepository;

    @Autowired
    public JpaHashtagRepositoryTest(@Qualifier("jpaHashtagRepository") HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    @Test
    void searchTag() {
        List<SearchHashtagInterface> result = hashtagRepository.searchTag("소통해요");

        result.forEach(res -> {
            System.out.println("res = " + res.getTag() + " " + res.getCount());
        });

    }
}