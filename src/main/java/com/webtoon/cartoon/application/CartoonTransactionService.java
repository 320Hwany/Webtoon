package com.webtoon.cartoon.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.repository.CartoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonTransactionService {

    private final CartoonRepository cartoonRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public void saveTransactionSet(CartoonSave cartoonSave, AuthorSession authorSession) {
        Author author = authorRepository.getById(authorSession.getId());
        Cartoon cartoon = Cartoon.getFromCartoonSaveAndAuthor(cartoonSave, author);
        cartoonRepository.save(cartoon);
    }

    @Transactional
    public void updateTransactionSet(Long cartoonId, CartoonUpdate cartoonUpdate) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.update(cartoonUpdate);
    }

    @Transactional
    public void deleteTransactionSet(Long cartoonId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoonRepository.delete(cartoon);
    }
}
