package com.webtoon.cartoon.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonEnumField;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.util.enumerated.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonService {

    private final CartoonRepository cartoonRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public Cartoon save(CartoonSave cartoonSave, AuthorSession authorSession) {
        Author author = authorRepository.getById(authorSession.getId());
        Cartoon cartoon = Cartoon.getFromCartoonSaveAndAuthor(cartoonSave, author);
        return cartoonRepository.save(cartoon);
    }

    public Cartoon getByTitle(String title) {
        return cartoonRepository.getByTitle(title);
    }

    public List<Cartoon> findAllByGenre(Genre genre) {
        return cartoonRepository.findAllByGenre(genre);
    }

    public void checkAuthorityForCartoon(Long cartoonId, AuthorSession authorSession) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.checkAuthorityForCartoon(authorSession);
    }

    public void checkEnumTypeValid(CartoonEnumField cartoonEnumField) {
        Cartoon.checkEnumTypeValid(cartoonEnumField);
    }

    public Genre getGenreFromString(String genreString) {
        Genre genre = CartoonEnumField.getGenreFromString(genreString);
        return genre;
    }

    @Transactional
    public Cartoon update(Long cartoonId, CartoonUpdate cartoonUpdate) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.update(cartoonUpdate);
        return cartoon;
    }
}
