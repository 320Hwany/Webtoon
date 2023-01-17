package com.webtoon.cartoon.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.util.BaseTimeEntity;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public void update(CartoonUpdate cartoonUpdate) {
        this.title = cartoonUpdate.getTitle();
        this.dayOfTheWeek = DayOfTheWeek.valueOf(cartoonUpdate.getDayOfTheWeek());
        this.progress = Progress.valueOf(cartoonUpdate.getProgress());
    }

    public void checkAuthorityForCartoon(AuthorSession authorSession) {
        if (author.getNickName() != authorSession.getNickName()) {
            throw new CartoonForbiddenException();
        }
    }

    public static void checkEnumTypeValid(String inputDayOfWeek, String inputProgress) {
        DayOfTheWeek[] DayList = DayOfTheWeek.values();
        Progress[] progressList = Progress.values();

        Boolean isDayValid = checkDayValid(inputDayOfWeek, DayList);
        Boolean isProgressValid = checkProgressValid(inputProgress, progressList);

        if ((isDayValid == false) || (isProgressValid == false)) {
            throw new EnumTypeValidException(isDayValid, isProgressValid);
        }
    }

    public static Boolean checkDayValid(String inputDayOfWeek, DayOfTheWeek[] DayList) {
        for (DayOfTheWeek day : DayList) {
            if (inputDayOfWeek.equals(day.getValue())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean checkProgressValid(String inputProgress, Progress[] progressList) {
        for (Progress progress : progressList) {
            if (inputProgress.equals(progress.getValue())) {
                return true;
            }
        }
        return false;
    }
}
