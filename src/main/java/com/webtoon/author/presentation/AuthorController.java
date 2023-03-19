package com.webtoon.author.presentation;

import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSearchNickname;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.dto.response.AuthorListResult;
import com.webtoon.author.application.AuthorService;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.util.annotation.LoginForAuthor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
        authorService.signup(authorSignup);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/login")
    public ResponseEntity<AuthorResponse> login(@RequestBody @Valid AuthorLogin authorLogin,
                                                HttpServletRequest httpServletRequest) {
        AuthorResponse authorResponse = authorService.login(authorLogin, httpServletRequest);
        return ResponseEntity.ok(authorResponse);
    }

    @GetMapping("/author/nickname")
    public ResponseEntity<AuthorListResult> getAuthorListByNickname(
            @Valid @ModelAttribute AuthorSearchNickname authorSearchNickname) {

        List<AuthorCartoonResponse> authorCartoonResponseList =
                authorService.findAllByNicknameContains(authorSearchNickname);
        return ResponseEntity.ok(new AuthorListResult(authorCartoonResponseList.size(), authorCartoonResponseList));
    }

    @PatchMapping("/author")
    public ResponseEntity<AuthorResponse> update(@LoginForAuthor AuthorSession authorSession,
                                                 @RequestBody @Valid AuthorUpdate authorUpdate) {
        AuthorResponse authorResponse = authorService.update(authorSession.getId(), authorUpdate);
        return ResponseEntity.ok(authorResponse);
    }

    @DeleteMapping("/author")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        authorService.delete(authorSession, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/logout")
    public ResponseEntity<Void> logout(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        authorService.invalidateSession(authorSession, httpServletRequest);
        return ResponseEntity.ok().build();
    }
}
