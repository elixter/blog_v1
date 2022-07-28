package elixter.blog.repository.post;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Qualifier(value = "jpaPostRepository")
public class JpaPostRepository implements PostRepository {

    private final SpringDataJpaPostRepository repository;

    @Autowired
    public JpaPostRepository(SpringDataJpaPostRepository repository) {
        this.repository = repository;
    }


    @Override
    public Post save(Post post) {
        return repository.save(post);
    }

    @Override
    public void update(Post post) {
        Post target = repository.findById(post.getId()).orElseThrow();
        target.setTitle(post.getTitle());
        target.setCategory(post.getCategory());
        target.setContent(post.getContent());
        target.setThumbnail(post.getThumbnail());
        target.setPostImages(post.getPostImages());
        target.setHashtags(post.getHashtags());
        target.setUpdateAt(post.getUpdateAt());
    }

    @Override
    public Optional<Post> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Post> findByIdAndStatus(Long id, RecordStatus status) {
        return repository.findByIdAndStatus(id, status);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Post> findAllByStatus(RecordStatus status, Pageable pageable) {
        return repository.findAllByStatus(status, pageable);
    }

    @Override
    public Page<Post> findByCategory(String category, Pageable pageable) {
        return repository.findAllByCategory(category, pageable);
    }

    @Override
    public Page<Post> findByCategoryAndStatus(String category, RecordStatus status, Pageable pageable) {
        return repository.findAllByCategoryAndStatus(category, status, pageable);
    }

    @Override
    public Page<Post> findByHashtag(String hashtag, Pageable pageable) {
        return repository.findByHashtag(hashtag, pageable);
    }

    @Override
    public Page<Post> findByHashtagAndStatus(String hashtag, RecordStatus status, Pageable pageable) {
        return repository.findByHashtagAndStatus(hashtag, status, pageable);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
