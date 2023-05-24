package com.webtoon.member.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.webtoon.util.constant.ConstantValid.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLogin {

    @Email(message = EMAIL_VALID_MESSAGE)
    private String email;

    @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_VALID_MESSAGE)
    private String password;

    @Builder
    private MemberLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
