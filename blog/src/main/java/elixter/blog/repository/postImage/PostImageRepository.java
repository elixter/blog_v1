package elixter.blog.repository.postImage;

import elixter.blog.domain.postImage.PostImage;

import java.util.List;

public interface PostImageRepository {

    PostImage save(PostImage postImage);
    List<PostImage> saveAll(List<PostImage> postImages);

    void deleteByPostId(Long postId);
    void deleteByImageId(Long imageId);
}
