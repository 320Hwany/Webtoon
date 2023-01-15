package com.webtoon.author.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional
    public Author signup(AuthorSignup authorSignup) {
        return authorRepository.save(
                Author.builder()
                .nickName(authorSignup.getNickName())
                .email(authorSignup.getEmail())
                .password(authorSignup.getPassword())
                .build());
    }

    @Transactional
    public AuthorSession makeAuthorSession(AuthorLogin authorLogin) {
        Author author = authorRepository.getByEmailAndPassword(authorLogin.getEmail(), authorLogin.getPassword());
        return AuthorSession.builder()
                .id(author.getId())
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();
    }
}
