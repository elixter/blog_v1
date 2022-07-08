package elixter.blog.repository.hashtag;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.hashtag.HashtagCountInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaHashtagRepository extends JpaRepository<Hashtag, Long> {

    @Query("Select h.tag as tag, count(h) as count from Hashtag h where h.tag like concat('%', :tag, '%')  group by h.tag")
    List<HashtagCountInterface> searchTag(String tag);

    @Query("select h from Hashtag h where h.id = :id and h.status = 1")
    Optional<Hashtag> findById(Long id);

    @Query("select h from Hashtag h where h.tag = :tag and h.status = 1")
    List<Hashtag> findByTag(String tag);

    @Query("select h from Hashtag h where h.status = 1")
    List<Hashtag> findAll();

    @Query("select h from Hashtag h where h.post.id = :postId and h.status = 1")
    List<Hashtag> findByPostId(Long postId);

    @Modifying
    @Query("update Hashtag h set h.status = 0 where h.id = :id")
    void deleteById(Long id);

    @Modifying
    @Query("update Hashtag h set h.status = 0 where h.tag = :tag")
    void deleteByTag(String tag);

    @Modifying
    @Query("update Hashtag h set h.status = 0 where h.post.id = :postId")
    void deleteByPostId(Long postId);
}
