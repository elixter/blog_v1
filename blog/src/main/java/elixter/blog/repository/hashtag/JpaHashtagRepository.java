package elixter.blog.repository.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.dto.hashtag.SearchHashtagDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Qualifier("jpaHashtagRepository")
public interface JpaHashtagRepository extends HashtagRepository, JpaRepository<Hashtag, Long> {

    @Override
    List<Hashtag> saveAll(List<Hashtag> hashtag);

    @Override
    @Query("Select h.tag, count(h) as tag_count from Hashtag h where :tag like :tag% group by h.tag")
    List<SearchHashtagDto> searchTag(String tag);

    @Override
    void deleteById(Long id);

    @Override
    void deleteByTag(String tag);

    @Override
    void deleteByPostId(Long postId);
}
