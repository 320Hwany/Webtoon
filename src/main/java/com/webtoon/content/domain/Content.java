package com.webtoon.content.domain;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

import static com.webtoon.util.constant.Constant.TWO_WEEKS;
import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_FLOAT;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Content extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "content_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cartoon_id")
    private Cartoon cartoon;

    private String subTitle;

    private Long episode;

    private LocalDate registrationDate;
    private Float rating;

    @Builder
    public Content(Cartoon cartoon, String subTitle, Long episode, Float rating,
                   LocalDate registrationDate) {
        this.cartoon = cartoon;
        this.subTitle = subTitle;
        this.episode = episode;
        this.rating = rating;
        this.registrationDate = registrationDate;
    }

    public static Content getFromContentSaveAndCartoon(ContentSave contentSave, Cartoon cartoon) {
        return Content.builder()
                .cartoon(cartoon)
                .subTitle(contentSave.getSubTitle())
                .episode(contentSave.getEpisode())
                .registrationDate(contentSave.getRegistrationDate())
                .rating(ZERO_OF_TYPE_FLOAT)
                .build();
    }

    public void update(ContentUpdate contentUpdate) {
        this.subTitle = contentUpdate.getSubTitle();
        this.episode = contentUpdate.getEpisode();
        this.registrationDate = contentUpdate.getRegistrationDate();
    }

    public LocalDate getLockLocalDate(Long weeks) {
        return registrationDate.plusWeeks(weeks);
    }
}
