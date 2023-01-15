package com.webtoon.author.dto.response;

import com.webtoon.author.dto.request.AuthorSession;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorResponse {

    private String nickName;
    private String email;
    private String password;

    @Builder
    public AuthorResponse(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }
}
