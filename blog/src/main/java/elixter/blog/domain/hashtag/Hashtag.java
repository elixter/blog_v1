package elixter.blog.domain.hashtag;

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

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
        this.post.addHashtag(this);
    }

    public Hashtag() {
        this.post = new Post();
    }

    @Builder
    public Hashtag(Long id, String tag, Post post) {
        this.id = id;
        this.tag = tag;
        this.post = post;
    }
}
