package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final AuthorJpaRepository authorJpaRepository;

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
    public Optional<Author> findByNickName(String nickName) {
        return authorJpaRepository.findByNickName(nickName);
    }

    @Override
    public List<Author> findAllByNickNameContains(String nickName) {
        return authorJpaRepository.findAllByNickNameContains(nickName);
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
