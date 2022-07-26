package elixter.blog.dto.user;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class EmailCheckRequestDto {

    private String email;
    private String code;

    public EmailCheckRequestDto(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
