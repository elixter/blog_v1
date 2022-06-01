package elixter.blog.repository.hashtag;

import elixter.blog.dto.hashtag.SearchHashtagDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JpaHashtagRepositoryTest {

    private HashtagRepository hashtagRepository;

    @Autowired
    public JpaHashtagRepositoryTest(@Qualifier("jpaHashtagRepository") HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    @Test
    void searchTag() {
        List<SearchHashtagDto> result = hashtagRepository.searchTag("소통해요");

    }
}