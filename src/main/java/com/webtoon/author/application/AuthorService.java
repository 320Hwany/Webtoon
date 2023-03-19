package com.webtoon.author.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSearchNickname;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.author.exception.AuthorNotMatchException;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(AuthorSignup authorSignup) {
        checkDuplication(authorSignup);
        Author author = authorSignup.toEntity(passwordEncoder);
        authorRepository.save(author);
    }

    public void checkDuplication(AuthorSignup authorSignup) {
        Optional<Author> findAuthorByNickname = authorRepository.findByNickname(authorSignup.getNickname());
        Optional<Author> findAuthorByEmail = authorRepository.findByEmail(authorSignup.getEmail());
        if (findAuthorByNickname.isPresent() || findAuthorByEmail.isPresent()) {
            throw new AuthorDuplicationException();
        }
    }

    @Transactional
    public AuthorResponse login(AuthorLogin authorLogin, HttpServletRequest request) {
        AuthorSession authorSession = makeAuthorSession(authorLogin);
        authorSession.makeSession(request);
        return AuthorResponse.getFromAuthorSession(authorSession);
    }

    @Transactional
    public AuthorSession makeAuthorSession(AuthorLogin authorLogin) {
        Author author = authorRepository.getByEmail(authorLogin.getEmail());
        if (passwordEncoder.matches(authorLogin.getPassword(), author.getPassword())) {
            return AuthorSession.getFromAuthor(author);
        }
        throw new AuthorNotMatchException();
    }

    @Transactional
    public AuthorResponse update(Long authorId, AuthorUpdate authorUpdate) {
        Author author = authorRepository.getById(authorId);
        author.update(authorUpdate, passwordEncoder);
        return AuthorResponse.getFromAuthor(author);
    }

    @Transactional
    public void delete(AuthorSession authorSession, HttpServletRequest request) {
        Author author = authorRepository.getById(authorSession.getId());
        authorRepository.delete(author);
        authorSession.invalidateSession(request);
    }

    public void invalidateSession(AuthorSession authorSession, HttpServletRequest request) {
        authorSession.invalidateSession(request);
    }

    public List<AuthorCartoonResponse> findAllByNicknameContains(AuthorSearchNickname authorSearchNickname) {
        List<Author> authorList = authorRepository.findAllByNicknameContains(authorSearchNickname);
        return authorList.stream()
                .map(AuthorCartoonResponse::getFromAuthor)
                .collect(Collectors.toList());
    }
}
