package elixter.blog.repository.image;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.image.Image;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Primary
@Qualifier("jpaImageRepository")
public interface JpaImageRepository extends ImageRepository, JpaRepository<Image, Long> {

    @Override
    Image save(Image image);

    @Override
    List<Image> findAll();

    @Override
    List<Image> findByStatus(RecordStatus status);

    @Override
    @Query("select i from Image i join PostImage pi on i.id = pi.image.id where pi.post.id = :postId")
    List<Image> findByPostId(Long postId);

    @Override
    @Query("select i from Image i where i.storedName in :urlList")
    List<Image> findByStoredName(List<String> urlList);

    @Override
    Optional<Image> findByStoredName(String storedName);
}
