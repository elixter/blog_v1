package elixter.blog.hashtag;

public class Hashtag {
    private Long id;
    private String value;
    private Long postId;

    public Hashtag(Long id, String value, Long postId) {
        this.id = id;
        this.value = value;
        this.postId = postId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
