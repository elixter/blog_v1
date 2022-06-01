package elixter.blog.repository.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.dto.hashtag.SearchHashtagInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Qualifier("jpaHashtagRepository")
public interface JpaHashtagRepository extends HashtagRepository, JpaRepository<Hashtag, Long> {

    @Override
    @Modifying
    @Query(value = "insert into Hashtag values(:hashtag)", nativeQuery = true)
    List<Hashtag> saveBatch(List<Hashtag> hashtag);

    @Override
    @Query("Select h.tag as tag, count(h) as count from Hashtag h where h.tag like concat('%', :tag, '%')  group by h.tag")
    List<SearchHashtagInterface> searchTag(String tag);

    @Override
    void deleteById(Long id);

    @Override
    void deleteByTag(String tag);

    @Override
    void deleteByPostId(Long postId);
}
