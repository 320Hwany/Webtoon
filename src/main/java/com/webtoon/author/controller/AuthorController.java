package com.webtoon.author.controller;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.service.AuthorService;
import com.webtoon.util.annotation.LoginForAuthor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/author/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid AuthorSignup authorSignup) {
        authorService.signup(authorSignup);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/login")
    public ResponseEntity<AuthorResponse> login(@RequestBody @Valid AuthorLogin authorLogin,
                                                HttpServletRequest httpServletRequest) {
        AuthorSession authorSession = authorService.makeAuthorSession(authorLogin);
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("authorSession", authorSession);
        AuthorResponse authorResponse = authorSession.getAuthorResponse();

        return ResponseEntity.ok(authorResponse);
    }

    @PatchMapping("/author")
    public ResponseEntity<AuthorResponse> update(@LoginForAuthor AuthorSession authorSession,
                                                 @RequestBody @Valid AuthorUpdate authorUpdate) {
        Author author = authorService.update(authorSession, authorUpdate);
        AuthorResponse authorResponse = author.getAuthorResponse();

        return ResponseEntity.ok(authorResponse);
    }

    @DeleteMapping("/author")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest request) {
        authorService.delete(authorSession);
        HttpSession session = request.getSession(false);
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/logout")
    public ResponseEntity<Void> logout(@LoginForAuthor AuthorSession authorSession,
                       HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
