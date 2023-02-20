package com.webtoon.content.service;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentGet;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.content.dto.request.ContentUpdateSet;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.member.domain.Member;
import com.webtoon.member.exception.LackOfCoinException;
import com.webtoon.util.ServiceTest;
import com.webtoon.util.constant.Constant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.TWO_WEEKS;
import static com.webtoon.util.constant.Constant.ZERO_OF_TYPE_LONG;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ContentServiceTest extends ServiceTest {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentTransactionService contentTransactionService;

    @Test
    @DisplayName("cartoonId, ContentSave로부터 그 만화에 해당하는 컨텐츠를 추가합니다")
    void saveTransactionSet200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 2, 20))
                .build();

        // when
        contentTransactionService.saveTransactionSet(cartoon.getId(), contentSave);

        // then
        assertThat(contentRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("cartoonId에 해당하는 만화가 없다면 예외가 발생합니다")
    void saveTransactionSet404() {
        // given
        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 2, 20))
                .build();

        // expected
        Assertions.assertThrows(CartoonNotFoundException.class,
                () -> contentTransactionService.saveTransactionSet(9999L, contentSave));
    }

    @Test
    @DisplayName("입력한 정보에 맞는 컨텐츠가 2주가 되지 않았다면 200코인을 차감하고 보여줍니다")
    void getContentTransactionSetPayCoin() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = Member.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(10000L)
                .build();

        memberRepository.save(member);

        ContentGet contentGet = ContentGet.builder()
                .memberSessionId(member.getId())
                .cartoonId(cartoon.getId())
                .contentEpisode(content.getEpisode())
                .build();

        // when
        Content findContent = contentTransactionService.getContentTransactionSet(contentGet);

        // then
        Member findMember = memberRepository.getById(member.getId());

        assertThat(findContent.getId()).isEqualTo(content.getId());
        assertThat(findMember.getCoin()).isEqualTo(9800);
    }

    @Test
    @DisplayName("입력한 정보에 맞는 컨텐츠가 2주가 넘었다면 무료로 보여줍니다")
    void getContentTransactionSet() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = Content.builder()
                .cartoon(cartoon)
                .subTitle("만화 부제")
                .episode(1)
                .rating(9.8)
                .registrationDate(LocalDate.now().minusMonths(1))
                .build();

        contentRepository.save(content);

        Member member = Member.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(10000L)
                .build();

        memberRepository.save(member);

        ContentGet contentGet = ContentGet.builder()
                .memberSessionId(member.getId())
                .cartoonId(cartoon.getId())
                .contentEpisode(content.getEpisode())
                .build();

        // when
        Content findContent = contentTransactionService.getContentTransactionSet(contentGet);

        // then
        Member findMember = memberRepository.getById(member.getId());

        assertThat(findContent.getId()).isEqualTo(content.getId());
        assertThat(findMember.getCoin()).isEqualTo(10000);
    }

    @Test
    @DisplayName("입력한 정보에 맞는 컨텐츠가 없다면 예외가 발생합니다")
    void getContentTransactionSet404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = Member.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(10000L)
                .build();

        memberRepository.save(member);

        ContentGet contentGetWithoutCartoon = ContentGet.builder()
                .memberSessionId(member.getId())
                .cartoonId(9999L)
                .contentEpisode(content.getEpisode())
                .build();

        ContentGet contentGetWithoutContentEpisode = ContentGet.builder()
                .memberSessionId(member.getId())
                .cartoonId(cartoon.getId())
                .contentEpisode(9999)
                .build();

        // expected
        assertThrows(ContentNotFoundException.class,
                () -> contentTransactionService.getContentTransactionSet(contentGetWithoutCartoon));
        assertThrows(ContentNotFoundException.class,
                () -> contentTransactionService.getContentTransactionSet(contentGetWithoutContentEpisode));
    }

    @Test
    @DisplayName("코인이 부족하다면 2주가 안된 컨텐츠를 볼 수 없습니다")
    void validatePreviewContent() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = Member.builder()
                .nickname("회원 닉네임")
                .email("yhwjd@naver.com")
                .password(passwordEncoder.encode("1234"))
                .coin(ZERO_OF_TYPE_LONG)
                .build();

        memberRepository.save(member);

        ContentGet contentGet = ContentGet.builder()
                .memberSessionId(member.getId())
                .cartoonId(cartoon.getId())
                .contentEpisode(content.getEpisode())
                .build();

        // expected
        assertThrows(LackOfCoinException.class,
                () -> contentTransactionService.getContentTransactionSet(contentGet));
    }

    @Test
    @DisplayName("Content를 수정합니다 - 성공")
    void update200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30)
                .registrationDate(LocalDate.of(1999, 3, 20))
                .build();

        ContentUpdateSet contentUpdateSet = ContentUpdateSet.builder()
                .cartoonId(cartoon.getId())
                .contentEpisode(content.getEpisode())
                .contentUpdate(contentUpdate)
                .build();

        // when
        contentTransactionService.updateTransactionSet(contentUpdateSet);

        // then
        Content findContent = contentRepository.getById(content.getId());
        assertThat(findContent.getCartoon().getId()).isEqualTo(cartoon.getId());
        assertThat(findContent.getSubTitle()).isEqualTo("수정 부제입니다");
        assertThat(findContent.getEpisode()).isEqualTo(30);
        assertThat(findContent.getRegistrationDate()).isEqualTo(LocalDate.of(1999, 3, 20));
    }

    @Test
    @DisplayName("만화에 대한 에피소드가 존재하면 컨텐츠를 가져옵니다")
    void findByCartoonAndEpisode200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        // when
        Content findContent = contentService.findByCartoonIdAndEpisode(cartoon.getId(), content.getEpisode());

        // then
        assertThat(findContent.getId()).isEqualTo(content.getId());
    }

    @Test
    @DisplayName("만화가 존재하지 않거나 에피소드가 존재하지 않으면 예외가 발생합니다")
    void findByCartoonAndEpisode404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        // expected
        assertThrows(ContentNotFoundException.class,
                () -> contentService.findByCartoonIdAndEpisode(9999L, content.getEpisode()));
        assertThrows(ContentNotFoundException.class,
                () -> contentService.findByCartoonIdAndEpisode(cartoon.getId(), 9999));
    }
}