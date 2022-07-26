package elixter.blog.domain.mail;

import elixter.blog.constants.RecordStatus;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mails")
@NoArgsConstructor
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String sender;

    @Column(length = 50)
    private String receiver;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private RecordStatus status;

    @Builder
    public Mail(Long id, String sender, String receiver, String title, String content, RecordStatus status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.content = content;
        this.status = status;
    }
}
