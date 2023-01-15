package com.webtoon.cartoon.service;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.repository.CartoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartoonService {

    private final CartoonRepository cartoonRepository;

    public void save(CartoonSave cartoonSave) {
        Cartoon cartoon = new Cartoon(cartoonSave);
        cartoonRepository.save(cartoon);
    }
}
