package com.webtoon.content.domain;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

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

    private int episode;

    private LocalDate registrationDate;
    private double rating;

    @Builder
    public Content(Cartoon cartoon, String subTitle, int episode, double rating,
                   LocalDate registrationDate) {
        this.cartoon = cartoon;
        this.subTitle = subTitle;
        this.episode = episode;
        this.rating = rating;
        this.registrationDate = registrationDate;
    }

    public void update(ContentUpdate contentUpdate) {
        this.subTitle = contentUpdate.getSubTitle();
        this.episode = contentUpdate.getEpisode();
        this.registrationDate = contentUpdate.getRegistrationDate();
    }

    public LocalDate getLockLocalDate(long weeks) {
        return registrationDate.plusWeeks(weeks);
    }
}
