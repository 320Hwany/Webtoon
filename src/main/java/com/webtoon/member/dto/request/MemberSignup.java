package com.webtoon.member.dto.request;

import com.webtoon.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_LONG;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignup {

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    @Email(message = "이메일을 입력해주세요")
    private String email;

    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣0-9]{1,20}$", message = "영문/한글/숫자 1~20자 이내로 작성해주세요")
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .password(passwordEncoder.encode(password))
                .birthDate(birthDate)
                .coin(ZERO_OF_TYPE_LONG)
                .build();
    }
}
