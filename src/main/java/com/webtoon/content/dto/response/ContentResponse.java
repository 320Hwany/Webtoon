package com.webtoon.content.dto.response;

import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.content.domain.Content;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ContentResponse {

    private CartoonResponse cartoonResponse;

    private String subTitle;

    private Integer episode;
    private float rating;

    private LocalDate registrationDate;

    @Builder
    public ContentResponse(CartoonResponse cartoonResponse, String subTitle, Integer episode,
                           float rating, LocalDate registrationDate) {
        this.cartoonResponse = cartoonResponse;
        this.subTitle = subTitle;
        this.episode = episode;
        this.rating = rating;
        this.registrationDate = registrationDate;
    }

    public static ContentResponse getFromContent(Content content) {
        return ContentResponse.builder()
                .cartoonResponse(CartoonResponse.getFromCartoon(content.getCartoon()))
                .subTitle(content.getSubTitle())
                .episode(content.getEpisode())
                .rating(content.getRating())
                .registrationDate(content.getRegistrationDate())
                .build();
    }
}
