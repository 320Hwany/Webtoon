package com.webtoon.cartoon.domain;

import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.util.DomainTest;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Genre;
import com.webtoon.util.enumerated.Progress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CartoonSearchTest extends DomainTest {

    @Test
    @DisplayName("CartoonSearchDto로부터 CartoonSearch를 생성합니다")
    void getByCartoonSearchDto() {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(1)
                .title("찾는 만화 제목")
                .nickname("작가 닉네임")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        // when
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);

        // then
        assertThat(cartoonSearch.getLimit()).isEqualTo(20);
        assertThat(cartoonSearch.getPage()).isEqualTo(1);
        assertThat(cartoonSearch.getTitle()).isEqualTo("찾는 만화 제목");
        assertThat(cartoonSearch.getNickname()).isEqualTo("작가 닉네임");
        assertThat(cartoonSearch.getDayOfTheWeek()).isEqualTo(DayOfTheWeek.MON);
        assertThat(cartoonSearch.getProgress()).isEqualTo(Progress.SERIALIZATION);
        assertThat(cartoonSearch.getGenre()).isEqualTo(Genre.ROMANCE);
    }

    @Test
    @DisplayName("CartoonSearch정보로 부터 Offset을 계산합니다")
    void getOffset() {
        // given
        CartoonSearchDto cartoonSearchDto = CartoonSearchDto.builder()
                .page(2)
                .title("찾는 만화 제목")
                .nickname("작가 닉네임")
                .dayOfTheWeek("MON")
                .progress("SERIALIZATION")
                .genre("ROMANCE")
                .build();

        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        Integer page = cartoonSearch.getPage();
        Integer limit = cartoonSearch.getLimit();

        // when
        Integer offset = cartoonSearch.getOffset();

        // then
        assertThat(offset).isEqualTo(Math.max(page - 1, 0) * limit);
    }
}
