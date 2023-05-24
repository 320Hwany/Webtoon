package com.webtoon.author.dto.request;

import com.webtoon.author.domain.Author;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.webtoon.util.constant.ConstantValid.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorSignup {

    @NotBlank(message = NICKNAME_VALID_MESSAGE)
    private String nickname;

    @Email(message = EMAIL_VALID_MESSAGE)
    private String email;

    @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_VALID_MESSAGE)
    private String password;

    @Builder
    private AuthorSignup(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public Author toEntity(PasswordEncoder passwordEncoder) {
        return Author.builder()
                .nickname(nickname)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
    }
}
