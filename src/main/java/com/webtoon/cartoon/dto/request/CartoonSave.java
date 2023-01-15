package com.webtoon.cartoon.dto.request;

import com.webtoon.util.embedded.DayOfTheWeek;
import com.webtoon.util.embedded.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CartoonSave {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "무슨 요일 웹툰인지 입력해주세요")
    private DayOfTheWeek dayOfTheWeek;

    @NotBlank(message = "웹툰의 현재 진행 상황을 알려주세요")
    private Progress progress;

    @Builder
    public CartoonSave(String title, DayOfTheWeek dayOfTheWeek, Progress progress) {
        this.title = title;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
    }
}
