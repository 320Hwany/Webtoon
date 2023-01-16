package com.webtoon.cartoon.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.util.BaseTimeEntity;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Cartoon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cartoon_id")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private DayOfTheWeek dayOfTheWeek;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @Builder
    public Cartoon(String title, DayOfTheWeek dayOfTheWeek, Progress progress, Author author) {
        this.title = title;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.author = author;
    }

    public static Cartoon getFromCartoonSaveAndAuthor(CartoonSave cartoonSave, Author author) {
        return Cartoon.builder()
                .title(cartoonSave.getTitle())
                .dayOfTheWeek(DayOfTheWeek.valueOf(cartoonSave.getDayOfTheWeek()))
                .progress(Progress.valueOf(cartoonSave.getProgress()))
                .author(author)
                .build();
    }

    public static void checkEnumTypeValid(String inputDayOfWeek, String inputProgress) {
        DayOfTheWeek[] DayList = DayOfTheWeek.values();
        Progress[] progressList = Progress.values();
        Boolean isDayValid = false;
        Boolean isProgressValid = false;

        for (DayOfTheWeek day : DayList) {
            if (inputDayOfWeek.equals(day.getValue())) {
                isDayValid = true;
                break;
            }
        }
        for (Progress progress : progressList) {
            if (inputProgress.equals(progress.getValue())) {
                isProgressValid = true;
                break;
            }
        }

        if ((isDayValid == false) || (isProgressValid == false)) {
            throw new EnumTypeValidException(isDayValid, isProgressValid);
        }
    }
}
