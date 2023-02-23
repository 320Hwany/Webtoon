package com.webtoon.cartoon.application;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
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

    @Transactional
    public void save(Cartoon cartoon) {
        cartoonRepository.save(cartoon);
    }

    public Cartoon getById(Long id) {
        return cartoonRepository.getById(id);
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

    public List<Cartoon> findAllByDayOfTheWeek(CartoonSearch cartoonSearch) {
        return cartoonRepository.findAllByDayOfTheWeek(cartoonSearch);
    }

    public void validateAuthorityForCartoon(AuthorSession authorSession, Long cartoonId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.validateAuthorityForCartoon(authorSession);
    }

    public void validateGenreValid(String genre) {
        if (!Genre.validateValid(genre)) {
            throw new EnumTypeValidException(false, false, true);
        }
    }

    @Transactional
    public void delete(Cartoon cartoon) {
        cartoonRepository.delete(cartoon);
    }

    @Transactional
    public void addLike(Cartoon cartoon) {
        cartoon.addLike();
    }
}
