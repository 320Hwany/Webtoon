package com.webtoon.author.dto.request;

import com.webtoon.util.constant.ConstantValid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import static com.webtoon.util.constant.ConstantValid.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorLogin {

    @Email(message = EMAIL_VALID_MESSAGE)
    private String email;

    @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_VALID_MESSAGE)
    private String password;

    @Builder
    private AuthorLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
