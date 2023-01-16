package com.webtoon.author.dto.request;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.response.AuthorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AuthorSignup {

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickName;

    @Email(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Builder
    public AuthorSignup(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public Author toEntity() {
        return Author.builder()
                .nickName(nickName)
                .email(email)
                .password(password)
                .build();
    }
}
