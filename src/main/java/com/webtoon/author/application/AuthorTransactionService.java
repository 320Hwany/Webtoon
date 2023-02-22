package com.webtoon.author.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorTransactionService {

    private final AuthorService authorService;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public void signupSet(AuthorSignup authorSignup) {
        authorService.checkDuplication(authorSignup);
        authorService.signup(authorSignup);
    }

    public AuthorResponse loginSet(AuthorLogin authorLogin, HttpServletRequest httpServletRequest) {
        AuthorSession authorSession = authorService.makeAuthorSession(authorLogin);
        authorService.makeSessionForAuthorSession(authorSession, httpServletRequest);
        return AuthorResponse.getFromAuthorSession(authorSession);
    }

    @Transactional
    public AuthorResponse updateSet(Long authorId, AuthorUpdate authorUpdate) {
        Author author = authorService.getById(authorId);
        author.update(authorUpdate, passwordEncoder);
        AuthorResponse authorResponse = AuthorResponse.getFromAuthor(author);
        return authorResponse;
    }

    @Transactional
    public void deleteSet(AuthorSession authorSession, HttpServletRequest httpServletRequest) {
        Author author = authorService.getById(authorSession.getId());
        authorService.delete(author);
        authorService.invalidateSession(authorSession, httpServletRequest);
    }

    public List<AuthorCartoonResponse> findAllByNicknameContains(CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        return authorService.findAllByNicknameContains(cartoonSearch);
    }
}
