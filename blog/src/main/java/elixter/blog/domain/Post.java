package elixter.blog.domain;

import elixter.blog.Constants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Post {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String thumbnail;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public Post() {
        status = Constants.recordStatusExist;
        createAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    public Post(Long id, String title, String content, String category, String thumbnail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = true;
        Post _obj = (Post)obj;

        if (obj instanceof Post) {
            if (!this.id.equals(((Post) obj).id) ||
                    !this.content.equals(((Post) obj).content) ||
                    !this.category.equals(((Post) obj).category) ||
                    !this.thumbnail.equals(((Post) obj).thumbnail)
            ) {
                result = false;
            }
        }
        else {
            result = false;
        }

        return result;
    }
}

