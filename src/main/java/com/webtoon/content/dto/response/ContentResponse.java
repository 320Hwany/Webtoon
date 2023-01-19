package com.webtoon.content.dto.response;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.content.domain.Content;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ContentResponse {

    private Cartoon cartoon;

    private String subTitle;

    private Integer episode;
    private float rating;

    private LocalDate registrationDate;

    @Builder
    public ContentResponse(Cartoon cartoon, String subTitle, Integer episode,
                           float rating, LocalDate registrationDate) {
        this.cartoon = cartoon;
        this.subTitle = subTitle;
        this.episode = episode;
        this.rating = rating;
        this.registrationDate = registrationDate;
    }

    public static ContentResponse getFromContent(Content content) {
        return ContentResponse.builder()
                .cartoon(content.getCartoon())
                .subTitle(content.getSubTitle())
                .episode(content.getEpisode())
                .rating(content.getRating())
                .registrationDate(content.getRegistrationDate())
                .build();
    }
}
