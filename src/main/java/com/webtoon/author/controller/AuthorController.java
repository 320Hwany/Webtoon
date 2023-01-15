package com.webtoon.author.controller;

import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/author/signup")
    public void signup(@RequestBody @Valid AuthorSignup authorSignup) {
        authorService.signup(authorSignup);
    }

    @PostMapping("/author/login")
    public ResponseEntity<AuthorResponse> login(@RequestBody @Valid AuthorLogin authorLogin,
                                                HttpServletRequest httpServletRequest) {
        AuthorSession authorSession = authorService.makeAuthorSession(authorLogin);
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("authorSession", authorSession);
        return ResponseEntity.ok(
                AuthorResponse.builder()
                        .nickName(authorSession.getNickName())
                        .email(authorSession.getEmail())
                        .password(authorSession.getPassword())
                        .build());
    }
}
