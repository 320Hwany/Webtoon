package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNickname(String nickname);
    Optional<Author> findByEmail(String email);

    List<Author> findAllByNicknameContains(String nickname, Pageable pageable);
}
