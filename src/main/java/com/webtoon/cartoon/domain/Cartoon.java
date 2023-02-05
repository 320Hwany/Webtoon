package com.webtoon.cartoon.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.dto.request.CartoonEnumField;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.util.BaseTimeEntity;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Objects;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @Enumerated(EnumType.STRING)
    private DayOfTheWeek dayOfTheWeek;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Float rating;
    private Long likes;

    @Builder
    public Cartoon(String title, Author author, DayOfTheWeek dayOfTheWeek, Progress progress,
                   Genre genre, Float rating, Long likes) {
        this.title = title;
        this.author = author;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
        this.rating = rating;
        this.likes = likes;
    }

    public static Cartoon getFromCartoonSaveAndAuthor(CartoonSave cartoonSave, Author author) {
        return Cartoon.builder()
                .title(cartoonSave.getTitle())
                .author(author)
                .dayOfTheWeek(DayOfTheWeek.valueOf(cartoonSave.getDayOfTheWeek()))
                .progress(Progress.valueOf(cartoonSave.getProgress()))
                .genre(Genre.valueOf(cartoonSave.getGenre()))
                .build();
    }

    public static void validateEnumTypeValid(CartoonEnumField cartoonEnumField) {
        Boolean isDayValid = validateDayValid(cartoonEnumField.getDayOfTheWeek());
        Boolean isProgressValid = validateProgressValid(cartoonEnumField.getProgress());
        Boolean isGenreValid = validateGenreValid(cartoonEnumField.getGenre());

        if ((isDayValid == false) || (isProgressValid == false) || (isGenreValid == false)) {
            throw new EnumTypeValidException(isDayValid, isProgressValid, isGenreValid);
        }
    }

    public static Boolean validateDayValid(String inputDayOfWeek) {
        DayOfTheWeek[] DayList = DayOfTheWeek.values();
        for (DayOfTheWeek day : DayList) {
            if (inputDayOfWeek.equals(day.getValue())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean validateProgressValid(String inputProgress) {
        Progress[] progressList = Progress.values();
        for (Progress progress : progressList) {
            if (inputProgress.equals(progress.getValue())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean validateGenreValid(String inputGenre) {
        Genre[] genreList = Genre.values();
        for (Genre genre : genreList) {
            if (inputGenre.equals(genre.getValue())) {
                return true;
            }
        }
        return false;
    }

    public void validateAuthorityForCartoon(AuthorSession authorSession) {
        if (author.getId() != authorSession.getId()) {
            throw new CartoonForbiddenException();
        }
    }

    public void update(CartoonUpdate cartoonUpdate) {
        this.title = cartoonUpdate.getTitle();
        this.dayOfTheWeek = DayOfTheWeek.valueOf(cartoonUpdate.getDayOfTheWeek());
        this.progress = Progress.valueOf(cartoonUpdate.getProgress());
        this.genre = Genre.valueOf(cartoonUpdate.getGenre());
    }

    public void addLike() {
        this.likes += 1;
    }
}
