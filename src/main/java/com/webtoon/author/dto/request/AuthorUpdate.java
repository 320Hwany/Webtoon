package com.webtoon.author.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.webtoon.util.constant.ConstantValid.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorUpdate {

    @NotBlank(message = NICKNAME_VALID_MESSAGE)
    private String nickname;

    @Email(message = EMAIL_VALID_MESSAGE)
    private String email;

    @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_VALID_MESSAGE)
    private String password;

    @Builder
    private AuthorUpdate(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
