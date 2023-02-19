package com.webtoon.cartoon.service;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
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
