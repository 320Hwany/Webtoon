package com.webtoon.author.controller;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.dto.response.AuthorResult;
import com.webtoon.author.service.AuthorService;
import com.webtoon.util.annotation.LoginForAuthor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/author/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid AuthorSignup authorSignup) {
        authorService.checkDuplication(authorSignup);
        authorService.signup(authorSignup);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/login")
    public ResponseEntity<AuthorResponse> login(@RequestBody @Valid AuthorLogin authorLogin,
                                                HttpServletRequest httpServletRequest) {
        AuthorSession authorSession = authorService.makeAuthorSession(authorLogin);
        authorService.makeSessionForAuthorSession(authorSession, httpServletRequest);
        AuthorResponse authorResponse = AuthorResponse.getFromAuthorSession(authorSession);
        return ResponseEntity.ok(authorResponse);
    }

    @GetMapping("/author/nickname")
    public ResponseEntity<AuthorResult> getAuthorBynickname(@RequestParam String nickname) {
        List<Author> authorList = authorService.findAllBynicknameContains(nickname);
        List<AuthorResponse> authorResponseList = AuthorResponse.getFromAuthorList(authorList);
        return ResponseEntity.ok(new AuthorResult(authorResponseList));
    }

    @PatchMapping("/author")
    public ResponseEntity<AuthorResponse> update(@LoginForAuthor AuthorSession authorSession,
                                                 @RequestBody @Valid AuthorUpdate authorUpdate) {
        authorService.update(authorSession, authorUpdate);
        Author author = authorService.getById(authorSession.getId());
        AuthorResponse authorResponse = AuthorResponse.getFromAuthor(author);
        return ResponseEntity.ok(authorResponse);
    }

    @DeleteMapping("/author")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        authorService.delete(authorSession);
        authorService.invalidateSession(authorSession, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/logout")
    public ResponseEntity<Void> logout(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        authorService.invalidateSession(authorSession, httpServletRequest);
        return ResponseEntity.ok().build();
    }
}
