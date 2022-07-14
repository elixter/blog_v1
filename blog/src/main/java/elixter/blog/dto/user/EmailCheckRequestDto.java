package elixter.blog.dto.user;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class EmailCheckRequestDto {

    private String code;

    public EmailCheckRequestDto(String code) {
        this.code = code;
    }
}
