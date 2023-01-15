package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNickName(String nickName);
    Optional<Author> findByEmail(String email);
    Optional<Author> findByEmailAndPassword(String email, String password);
}
