package com.webtoon.cartoon.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSave;
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

    @Transactional
    public void update(Cartoon cartoon, CartoonUpdate cartoonUpdate) {
        cartoon.update(cartoonUpdate);
    }

    @Transactional
    public void delete(Cartoon cartoon) {
        cartoonRepository.delete(cartoon);
    }

    public void validateAuthorityForCartoon(AuthorSession authorSession, Cartoon cartoon) {
        cartoon.validateAuthorityForCartoon(authorSession);
    }

    public void validateGenreValid(String genre) {
        if (Genre.validateValid(genre) == false) {
            throw new EnumTypeValidException(false, false, true);
        }
    }

    @Transactional
    public void addLike(Cartoon cartoon) {
        cartoon.addLike();
    }

    @Transactional
    public void calculateRatingAvg(Long cartoonId, int cartoonListSize, double rating) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        double sum = cartoon.calculateSum(cartoonListSize);
        cartoon.rating((sum + rating) / cartoonListSize);
    }
}
