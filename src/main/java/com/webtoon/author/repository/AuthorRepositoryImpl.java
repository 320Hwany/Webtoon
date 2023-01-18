package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.exception.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final AuthorJpaRepository authorJpaRepository;

    @Override
    public Author save(Author author) {
        return authorJpaRepository.save(author);
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
    public Author getByNickName(String nickName) {
        return authorJpaRepository.findByNickName(nickName)
                .orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public Optional<Author> findByEmail(String email) {
        return authorJpaRepository.findByEmail(email);
    }

    @Override
    public Author getByEmailAndPassword(String email, String password) {
        return authorJpaRepository.findByEmailAndPassword(email, password)
                .orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public Boolean checkAuthorPresent(AuthorSession authorSession) {
        authorJpaRepository.findByEmailAndPassword(authorSession.getEmail(), authorSession.getPassword())
                .orElseThrow(AuthorNotFoundException::new);
        return true;
    }


    @Override
    public void delete(Author author) {
        authorJpaRepository.delete(author);
    }
}
