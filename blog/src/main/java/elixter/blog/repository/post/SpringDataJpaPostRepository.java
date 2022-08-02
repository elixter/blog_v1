package elixter.blog.repository.post;

import elixter.blog.domain.RecordStatus;
import elixter.blog.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataJpaPostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndStatus(Long id, RecordStatus status);

    Page<Post> findAllByStatus(RecordStatus status, Pageable pageable);

    Page<Post> findAllByCategory(String category, Pageable pageable);

    Page<Post> findAllByCategoryAndStatus(String category, RecordStatus status, Pageable pageable);

    @Modifying
    @Query("update Post p set p.title = :#{#paramPost.title}, p.content = :#{#paramPost.content},  p.category = :#{#paramPost.category}, p.thumbnail = :#{#paramPost.thumbnail}, p.updateAt = :#{#paramPost.updateAt} where p.id = :#{#paramPost.id}")
    void update(@Param("paramPost") Post post);

    @Query("select p from Post p join Hashtag h on p.id = h.post.id where h.tag = :hashtag and p.status = 1")
    Page<Post> findByHashtag(String hashtag, Pageable pageable);

    @Query("select p from Post p join Hashtag h on p.id = h.post.id where h.tag = :hashtag and p.status = :status group by h.post.id")
    Page<Post> findByHashtagAndStatus(String hashtag, RecordStatus status, Pageable pageable);

    @Modifying
    @Query("update Post p set p.status = 0 where p.id = :id")
    void delete(@Param("id") Long id);
}
