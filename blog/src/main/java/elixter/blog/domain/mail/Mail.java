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

    private String title;
    private String content;

    @Enumerated(EnumType.ORDINAL)
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
