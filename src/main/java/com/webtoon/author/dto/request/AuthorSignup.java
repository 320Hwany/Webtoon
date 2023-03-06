package com.webtoon.author.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorSignup {

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    @Email(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣0-9]{1,20}$", message = "영문/한글/숫자 1~20자 이내로 작성해주세요")
    private String password;
}
