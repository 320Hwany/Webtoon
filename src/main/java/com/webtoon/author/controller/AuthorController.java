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
        authorSession.makeSession(httpServletRequest);
        AuthorResponse authorResponse = AuthorResponse.getFromAuthorSession(authorSession);
        return ResponseEntity.ok(authorResponse);
    }

    @PatchMapping("/author")
    public ResponseEntity<AuthorResponse> update(@LoginForAuthor AuthorSession authorSession,
                                                 @RequestBody @Valid AuthorUpdate authorUpdate) {
        Author author = authorService.update(authorSession, authorUpdate);
        AuthorResponse authorResponse = AuthorResponse.getFromAuthor(author);
        return ResponseEntity.ok(authorResponse);
    }

    @DeleteMapping("/author")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        authorService.delete(authorSession);
        authorSession.invalidateSession(httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/logout")
    public ResponseEntity<Void> logout(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        authorSession.invalidateSession(httpServletRequest);
        return ResponseEntity.ok().build();
    }
}
