package elixter.blog.repository.hashtagRepository;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.repository.hashtag.MemoryHashtagRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MemoryHashtagRepositoryTest {
    MemoryHashtagRepository repository = new MemoryHashtagRepository();

    @AfterEach
    void afterEach() {
        repository.clearStore();
    }

    @Test
    @DisplayName("해쉬태그 저장 테스트")
    void createTest() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.getPost().setId(1L);

        Hashtag result = repository.save(hashtag);

        Assertions.assertThat(result).isEqualTo(hashtag);
    }

    @Test
    @DisplayName("아이디로 해쉬태그 가져오기 테스트")
    void findByIdTest() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.getPost().setId(1L);
        repository.save(hashtag);

        Optional<Hashtag> result = repository.findById(hashtag.getId());
        Assertions.assertThat(result.get()).isEqualTo(hashtag);

        Optional<Hashtag> result2 = repository.findById(hashtag.getId() + 1);
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> result2.get());
    }

    @Test
    @DisplayName("태그로 해쉬태그 가져오기 테스트")
    void findByTagTest() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.getPost().setId(1L);
        repository.save(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTag("소통해요");
        hashtag2.getPost().setId(2L);
        repository.save(hashtag2);

        List<Hashtag> result = repository.findByTag("소통해요");
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result).contains(hashtag);
        Assertions.assertThat(result).contains(hashtag2);

        List<Hashtag> result2 = repository.findByTag("맞팔");
        Assertions.assertThat(result2.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("모든 해쉬태그 엔티티 가져오기 테스트")
    void findAllTest() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.getPost().setId(1L);
        repository.save(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTag("소통해요");
        hashtag2.getPost().setId(2L);
        repository.save(hashtag2);

        List<Hashtag> result = repository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 아이디로 가져오기 테스트")
    void findByPostIdTest() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.getPost().setId(555L);
        repository.save(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTag("소통해요");
        hashtag2.getPost().setId(556L);
        repository.save(hashtag2);

        List<Hashtag> result = repository.findByPostId(555L);
        Assertions.assertThat(result.size()).isEqualTo(1);

        List<Hashtag> result2 = repository.findByPostId(556L);
        Assertions.assertThat(result2.size()).isEqualTo(1);

        List<Hashtag> result3 = repository.findByPostId(557L);
        Assertions.assertThat(result3.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("아이디로 삭제 테스트")
    void deleteByIdTest() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.getPost().setId(555L);
        repository.save(hashtag);

        repository.deleteById(hashtag.getId());

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> repository.findById(hashtag.getId()).get());
    }

    @Test
    @DisplayName("태그로 삭제 테스트")
    void deleteByTagTest() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.getPost().setId(555L);
        repository.save(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTag("소통해요");
        hashtag2.getPost().setId(556L);
        repository.save(hashtag2);

        repository.deleteByTag("소통해요");

        List<Hashtag> result = repository.findByTag("소통해요");
        Assertions.assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 아이디로 삭제 테스트")
    void deleteByPostId() {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag("소통해요");
        hashtag.getPost().setId(666L);
        repository.save(hashtag);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setTag("소통해요");
        hashtag2.getPost().setId(666L);
        repository.save(hashtag2);

        repository.deleteByPostId(666L);
        List<Hashtag> result = repository.findByPostId(666L);
        Assertions.assertThat(result.size()).isEqualTo(0);
    }
}
