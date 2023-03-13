package com.webtoon.content.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentGet;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.content.dto.request.ContentUpdateSet;
import com.webtoon.content.dto.response.ContentResponse;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.member.domain.Member;
import com.webtoon.member.exception.LackOfCoinException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.webtoon.util.constant.ConstantCommon.ZERO_OF_TYPE_LONG;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ContentServiceTest extends ServiceTest {

    @Autowired
    private ContentService contentService;


    @Test
    @DisplayName("만화의 컨텐츠를 한 페이지 가져옵니다")
    void findAllByCartoonId() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);

        List<Content> contentList = IntStream.range(1, 11)
                .mapToObj(i -> Content.builder()
                        .cartoon(cartoon)
                        .episode(i)
                        .build())
                .collect(Collectors.toList());

        contentRepository.saveAll(contentList);

        Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "id");

        // when
        List<ContentResponse> contentResponseList = contentService.findAllByCartoonId(cartoon.getId(), pageable);

        // then
        assertThat(contentResponseList.size()).isEqualTo(10);
        assertThat(contentResponseList.get(0).getEpisode()).isEqualTo(10);
    }

    @Test
    @DisplayName("cartoonId, ContentSave로부터 그 만화에 해당하는 컨텐츠를 추가합니다")
    void saveSet200() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
        Cartoon cartoon = saveCartoonInRepository(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 2, 20))
                .build();

        // when
        contentService.saveSet(authorSession, cartoon.getId(), contentSave);

        // then
        assertThat(contentRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("cartoonId에 해당하는 만화가 없다면 예외가 발생합니다")
    void saveSet404() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 2, 20))
                .build();

        // expected
        Assertions.assertThrows(CartoonNotFoundException.class,
                () -> contentService.saveSet(authorSession,9999L, contentSave));
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
        Content findContent = contentService.getContentTransactionSet(contentGet);

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
        Content findContent = contentService.getContentTransactionSet(contentGet);

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
                () -> contentService.getContentTransactionSet(contentGetWithoutCartoon));
        assertThrows(ContentNotFoundException.class,
                () -> contentService.getContentTransactionSet(contentGetWithoutContentEpisode));
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
                () -> contentService.getContentTransactionSet(contentGet));
    }

    @Test
    @DisplayName("입력한 정보로 컨텐츠를 찾고 컨텐츠를 수정합니다")
    void update200() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
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
        contentService.update(authorSession, cartoon.getId(), contentUpdateSet);

        // then
        Content findContent = contentRepository.getById(content.getId());
        assertThat(findContent.getCartoon().getId()).isEqualTo(cartoon.getId());
        assertThat(findContent.getSubTitle()).isEqualTo("수정 부제입니다");
        assertThat(findContent.getEpisode()).isEqualTo(30);
        assertThat(findContent.getRegistrationDate()).isEqualTo(LocalDate.of(1999, 3, 20));
    }

    @Test
    @DisplayName("입력한 정보에 맞는 컨텐츠가 없다면 예외가 발생합니다")
    void update404() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
        Cartoon cartoon = saveCartoonInRepository(author);

        ContentUpdate contentUpdate = ContentUpdate.builder()
                .subTitle("수정 부제입니다")
                .episode(30)
                .registrationDate(LocalDate.of(1999, 3, 20))
                .build();

        ContentUpdateSet contentUpdateSet = ContentUpdateSet.builder()
                .cartoonId(9999L)
                .contentEpisode(9999)
                .contentUpdate(contentUpdate)
                .build();

        // expected
        assertThrows(ContentNotFoundException.class,
                () -> contentService.update(authorSession, cartoon.getId(), contentUpdateSet));
    }
}