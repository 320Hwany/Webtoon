package com.webtoon.author.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.member.exception.MemberUnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(AuthorSignup authorSignup) {
        Author author = Author.getFromAuthorSignup(authorSignup, passwordEncoder);
        authorRepository.save(author);
    }

    public List<Author> findAllBynicknameContains(String nickname) {
        return authorRepository.findAllBynicknameContains(nickname);
    }

    @Transactional
    public void update(AuthorSession authorSession, AuthorUpdate authorUpdate) {
        Author author = authorRepository.getById(authorSession.getId());
        author.update(authorUpdate, passwordEncoder);
    }

    public Author getById(Long authorId) {
        return authorRepository.getById(authorId);
    }

    @Transactional
    public void delete(AuthorSession authorSession) {
        Author author = authorRepository.getById(authorSession.getId());
        authorRepository.delete(author);
    }

    public void checkDuplication(AuthorSignup authorSignup) {
        Optional<Author> findAuthorBynickname = authorRepository.findBynickname(authorSignup.getNickname());
        Optional<Author> findAuthorByEmail = authorRepository.findByEmail(authorSignup.getEmail());
        if (findAuthorBynickname.isPresent() || findAuthorByEmail.isPresent()) {
            throw new AuthorDuplicationException();
        }
    }

    public AuthorSession makeAuthorSession(AuthorLogin authorLogin) {
        Author author = authorRepository.getByEmail(authorLogin.getEmail());
        if (passwordEncoder.matches(authorLogin.getPassword(), author.getPassword())) {
            AuthorSession authorSession = AuthorSession.getFromAuthor(author);
            return authorSession;
        }
        throw new MemberUnauthorizedException();
    }

    public void makeSessionForAuthorSession(AuthorSession authorSession, HttpServletRequest request) {
        authorSession.makeSession(request);
    }

    public void invalidateSession(AuthorSession authorSession, HttpServletRequest request) {
        authorSession.invalidateSession(request);
    }
}
