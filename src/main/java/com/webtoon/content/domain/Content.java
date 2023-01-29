package com.webtoon.content.domain;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Content {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "content_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cartoon_id")
    private Cartoon cartoon;

    private String subTitle;

    private Integer episode;
    private float rating;

    private LocalDate registrationDate;

    @Builder
    public Content(Cartoon cartoon, String subTitle, Integer episode, float rating,
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

    public LocalDate getLockLocalDate() {
        return registrationDate.plusWeeks(2);
    }

    public static Content getFromContentSaveAndCartoon(ContentSave contentSave, Cartoon cartoon) {
        return Content.builder()
                .cartoon(cartoon)
                .subTitle(contentSave.getSubTitle())
                .episode(contentSave.getEpisode())
                .registrationDate(contentSave.getRegistrationDate())
                .build();
    }
}
