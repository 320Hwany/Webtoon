package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository {

    Author save(Author author);

    Author getById(Long id);
    Optional<Author> findByNickName(String nickName);
    Author getByNickName(String nickName);

    Optional<Author> findByEmail(String email);

    Author getByEmailAndPassword(String email, String password);

    Boolean checkAuthorPresent(AuthorSession authorSession);

    void deleteAll();

    void delete(Author author);
}
