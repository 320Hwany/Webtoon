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


import static com.webtoon.util.constant.Constant.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
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

    private double rating;
    private long likes;

    @Builder
    public Cartoon(String title, Author author, DayOfTheWeek dayOfTheWeek, Progress progress,
                   Genre genre, double rating, long likes) {
        this.title = title;
        this.author = author;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.genre = genre;
        this.rating = rating;
        this.likes = likes;
    }

    public static Cartoon getFromCartoonSaveAndAuthor(CartoonSave cartoonSave, Author author) {
        Cartoon cartoon = Cartoon.builder()
                .title(cartoonSave.getTitle())
                .author(author)
                .dayOfTheWeek(DayOfTheWeek.valueOf(cartoonSave.getDayOfTheWeek()))
                .progress(Progress.valueOf(cartoonSave.getProgress()))
                .genre(Genre.valueOf(cartoonSave.getGenre()))
                .rating(ZERO_OF_TYPE_DOUBLE)
                .likes(ZERO_OF_TYPE_LONG)
                .build();

        author.getCartoonList().add(cartoon);
        return cartoon;
    }

    public static void validateEnumTypeValid(CartoonEnumField cartoonEnumField) {
        boolean isDayValid = DayOfTheWeek.validateValid(cartoonEnumField.getDayOfTheWeek());
        boolean isProgressValid = Progress.validateValid(cartoonEnumField.getProgress());
        boolean isGenreValid = Genre.validateValid(cartoonEnumField.getGenre());

        if ((isDayValid == false) || (isProgressValid == false) || (isGenreValid == false)) {
            throw new EnumTypeValidException(isDayValid, isProgressValid, isGenreValid);
        }
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

    public double calculateSum(long cartoonListSize) {
        return (cartoonListSize * rating);
    }

    public void rating(double rating) {
        this.rating = Math.round(rating * 100) / 100.0;
    }
}
