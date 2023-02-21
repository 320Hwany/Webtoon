package com.webtoon.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
public class MemberUpdate {

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    @Email(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Builder
    public MemberUpdate(String nickname, String email, String password, LocalDate birthDate) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }
}
