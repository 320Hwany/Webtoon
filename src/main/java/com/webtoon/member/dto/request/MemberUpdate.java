package com.webtoon.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

import static com.webtoon.util.constant.ConstantValid.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdate {

    @NotBlank(message = NICKNAME_VALID_MESSAGE)
    private String nickname;

    @Email(message = EMAIL_VALID_MESSAGE)
    private String email;

    @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_VALID_MESSAGE)
    private String password;

    @DateTimeFormat(pattern = YEAR_MONTH_DAY)
    private LocalDate birthDate;
}
