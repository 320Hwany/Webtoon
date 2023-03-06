package com.webtoon.member.dto.request;

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
public class MemberLogin {

    @Email(message = "이메일을 입력해주세요")
    private String email;

    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣0-9]{1,20}$", message = "영문/한글/숫자 1~20자 이내로 작성해주세요")
    private String password;
}
