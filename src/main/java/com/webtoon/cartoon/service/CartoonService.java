package com.webtoon.cartoon.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonEnumField;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.EnumTypeValidException;
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

    public List<Cartoon> findAllByTitle(CartoonSearch cartoonSearch) {
        return cartoonRepository.findAllByTitle(cartoonSearch);
    }

    public List<Cartoon> findAllByGenre(CartoonSearch cartoonSearch) {
        return cartoonRepository.findAllByGenre(cartoonSearch);
    }

    public List<Cartoon> findAllOrderByLikes(CartoonSearch cartoonSearch) {
        return cartoonRepository.findAllOrderByLikes(cartoonSearch);
    }

    public void checkAuthorityForCartoon(Long cartoonId, AuthorSession authorSession) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.checkAuthorityForCartoon(authorSession);
    }

    public void checkEnumTypeValid(CartoonEnumField cartoonEnumField) {
        Cartoon.checkEnumTypeValid(cartoonEnumField);
    }

    public void checkGenreValid(String genre) {
        if (Cartoon.checkGenreValid(genre) == false) {
            throw new EnumTypeValidException(false, false, true);
        }
    }

    @Transactional
    public Cartoon update(Long cartoonId, CartoonUpdate cartoonUpdate) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.update(cartoonUpdate);
        return cartoon;
    }
}
