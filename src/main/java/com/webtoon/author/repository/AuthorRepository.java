package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSearchNickname;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    void save(Author author);

    Author getById(Long id);

    Optional<Author> findByNickname(String nickname);

    List<Author> findAllByNicknameContains(AuthorSearchNickname authorSearchNickname);

    Author getByEmail(String email);

    Optional<Author> findByEmail(String email);

    void validateAuthorPresent(AuthorSession authorSession);

    void delete(Author author);

    void deleteAll();

    long count();
}
