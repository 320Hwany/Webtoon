package com.webtoon.author.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import static com.webtoon.util.constant.ConstantValid.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorSearchNickname {

    @Min(value = 0, message = PAGE_VALID_MESSAGE)
    private int page;

    @Min(value = 0, message = CARTOON_SIZE_VALID_MESSAGE)
    private int size;

    @NotBlank(message = NICKNAME_VALID_MESSAGE)
    private String nickname;

    @Builder
    private AuthorSearchNickname(int page, int size, String nickname) {
        this.page = page;
        this.size = size;
        this.nickname = nickname;
    }
}
