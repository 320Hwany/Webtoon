package com.webtoon.content.dto.response;

import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.content.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse {

    private CartoonResponse cartoonResponse;

    private String subTitle;

    private int episode;
    private double rating;

    private LocalDate registrationDate;

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
