package com.webtoon.cartoon.dto.response;

import com.webtoon.cartoon.dto.request.CartoonSave;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartoonResponse {

    private String title;

    public CartoonResponse(CartoonSave cartoonSave) {
        this.title = cartoonSave.getTitle();
    }
}
