package com.webtoon.author.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.domain.QAuthor;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.QCartoon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.webtoon.author.domain.QAuthor.*;
import static com.webtoon.cartoon.domain.QCartoon.*;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final AuthorJpaRepository authorJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(Author author) {
        authorJpaRepository.save(author);
    }

    @Override
    public Author getById(Long id) {
        return authorJpaRepository.findById(id)
                .orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public Optional<Author> findBynickname(String nickname) {
        return authorJpaRepository.findBynickname(nickname);
    }

    @Override
    public List<Author> findAllBynicknameContains(String nickname) {
        return authorJpaRepository.findAllBynicknameContains(nickname);
    }

    @Override
    public Author getByEmail(String email) {
        return authorJpaRepository.findByEmail(email)
                .orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public Optional<Author> findByEmail(String email) {
        return authorJpaRepository.findByEmail(email);
    }

    @Override
    public void validateAuthorPresent(AuthorSession authorSession) {
        authorJpaRepository.findById(authorSession.getId())
                .orElseThrow(AuthorNotFoundException::new);
    }


    @Override
    public void delete(Author author) {
        authorJpaRepository.delete(author);
    }

    @Override
    public long count() {
        return authorJpaRepository.count();
    }
}
