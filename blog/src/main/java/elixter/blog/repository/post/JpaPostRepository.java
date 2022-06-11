package elixter.blog.repository.post;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.post.Post;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


@Qualifier(value = "jpaPostRepository")
public interface JpaPostRepository extends PostRepository, JpaRepository<Post, Long> {

    @Override
    Post save(Post post);

    @Override
    @Modifying
    @Query("update Post p set p.title = :#{#paramPost.title}, p.content = :#{#paramPost.content},  p.category = :#{#paramPost.category}, p.thumbnail = :#{#paramPost.thumbnail}, p.updateAt = :#{#paramPost.updateAt} where p.id = :#{#paramPost.id}")
    void update(@Param("paramPost") Post post);

    @Override
    Optional<Post> findById(Long id);

    Optional<Post> findByIdAndStatus(Long id, RecordStatus status);

    @Override
    Page<Post> findAll(Pageable pageable);

    @Override
    Page<Post> findAllByStatus(RecordStatus status, Pageable pageable);

    @Override
    Page<Post> findByCategory(String category, Pageable pageable);

    @Override
    Page<Post> findByCategoryAndStatus(String category, RecordStatus status, Pageable pageable);

    @Override
    @Query("select p from Post p join Hashtag h on p.id = h.post.id where h.tag = :hashtag and p.status = 1")
    Page<Post> findByHashtag(String hashtag, Pageable pageable);

    @Override
    @Query("select p from Post p join Hashtag h on p.id = h.post.id where h.tag = :hashtag and p.status = :status group by h.post.id")
    Page<Post> findByHashtagAndStatus(String hashtag, RecordStatus status, Pageable pageable);

    @Override
    @Modifying
    @Query("update Post p set p.status = 0 where p.id = :id")
    void delete(@Param("id") Long id);
}