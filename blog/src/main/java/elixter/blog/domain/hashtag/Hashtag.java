package elixter.blog.domain.hashtag;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.post.Post;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "hashtags")
@ToString(exclude = "post")
@EqualsAndHashCode(exclude = "post")
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tag;
    private RecordStatus status;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public void setPost(Post post) {
        if (this.post != null) {
            this.post.getHashtags().remove(this);
        }
        this.post = post;

        if (!post.getHashtags().contains(this)) {
            this.post.addHashtag(this);
        }
    }

    public Hashtag() {
        this.post = new Post();
    }

    @Builder
    public Hashtag(Long id, String tag, RecordStatus status, Post post) {
        this.id = id;
        this.tag = tag;
        this.status = status;
        this.post = post;
    }
}
