package elixter.blog.domain.postImage;

import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "posts_images")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    public PostImage() {
    }

    @Builder
    public PostImage(Long id, Post post, Image image) {
        this.id = id;
        this.post = post;
        this.image = image;
    }

    public void changePost(Post post) {
        if (!post.getPostImages().contains(this)) {
            post.getPostImages().add(this);
        }

        if (this.post != null) {
            if (!this.post.equals(post)) {
                this.post = post;
            }
        } else {
            this.post = post;
        }
    }

    public void changeImage(Image image) {
        this.image = image;
    }
}
