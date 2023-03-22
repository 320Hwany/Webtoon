package com.webtoon.content.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.*;
import com.webtoon.content.dto.response.ContentResponse;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.member.domain.Member;
import com.webtoon.member.exception.LackOfCoinException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.webtoon.content.dto.request.ContentSaveSet.toContentSaveSet;
import static com.webtoon.util.constant.ConstantCommon.ZERO_OF_TYPE_LONG;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ContentServiceTest extends ServiceTest {

    @Autowired
    private ContentService contentService;

    @Test
    @DisplayName("입력한 정보로부터 그 만화에 해당하는 컨텐츠를 추가합니다")
    void save200() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);
        Cartoon cartoon = saveCartoonInRepository(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 2, 20))
                .build();

        ContentSaveSet contentSaveSet = toContentSaveSet(authorSession, cartoon.getId(), contentSave);

        // when
        contentService.save(contentSaveSet);

        // then
        assertThat(contentRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("cartoonId에 해당하는 만화가 없다면 예외가 발생합니다")
    void save404() {
        // given
        Author author = saveAuthorInRepository();
        AuthorSession authorSession = getAuthorSessionFromAuthor(author);

        ContentSave contentSave = ContentSave.builder()
                .subTitle("부제입니다")
                .episode(20)
                .registrationDate(LocalDate.of(2023, 2, 20))
                .build();

        ContentSaveSet contentSaveSet = toContentSaveSet(authorSession, 9999L, contentSave);

        // expected
        Assertions.assertThrows(CartoonNotFoundException.class,
                () -> contentService.save(contentSaveSet));
    }

    @Test
    @DisplayName("만화의 컨텐츠를 에피소드 역순으로 한 페이지 가져옵니다")
    void findAllForCartoon() {
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

        ContentGet contentGet = ContentGet.builder()
                .page(1)
                .size(20)
                .cartoonId(cartoon.getId())
                .build();

        // when
        List<ContentResponse> contentResponseList = contentService.findAllForCartoon(contentGet);

        // then
        assertThat(contentResponseList.size()).isEqualTo(10);
        assertThat(contentResponseList.get(0).getEpisode()).isEqualTo(10);
    }

    @Test
    @DisplayName("입력한 정보에 맞는 컨텐츠가 2주가 되지 않았다면 200코인을 차감하고 보여줍니다")
    void getEpisodePayCoin() {
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

        EpisodeGetSet episodeGetSet = EpisodeGetSet.builder()
                .memberSessionId(member.getId())
                .cartoonId(cartoon.getId())
                .contentEpisode(content.getEpisode())
                .build();

        // when
        ContentResponse contentResponse = contentService.getEpisode(episodeGetSet);

        // then
        Member findMember = memberRepository.getById(member.getId());

        assertThat(findMember.getCoin()).isEqualTo(9800);
        assertThat(contentResponse.getEpisode()).isEqualTo(1);
    }

    @Test
    @DisplayName("입력한 정보에 맞는 컨텐츠가 2주가 넘었다면 무료로 보여줍니다")
    void getEpisodeForFree() {
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

        EpisodeGetSet episodeGetSet = EpisodeGetSet.builder()
                .memberSessionId(member.getId())
                .cartoonId(cartoon.getId())
                .contentEpisode(content.getEpisode())
                .build();

        // when
        ContentResponse contentResponse = contentService.getEpisode(episodeGetSet);

        // then
        Member findMember = memberRepository.getById(member.getId());

        assertThat(findMember.getCoin()).isEqualTo(10000);
        assertThat(contentResponse.getEpisode()).isEqualTo(1);
    }

    @Test
    @DisplayName("코인이 부족하다면 2주가 안된 컨텐츠를 볼 수 없습니다")
    void LackOfCoin() {
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

        EpisodeGetSet episodeGet = EpisodeGetSet.builder()
                .memberSessionId(member.getId())
                .cartoonId(cartoon.getId())
                .contentEpisode(content.getEpisode())
                .build();

        // expected
        assertThrows(LackOfCoinException.class,
                () -> contentService.getEpisode(episodeGet));
    }

    @Test
    @DisplayName("입력한 정보에 맞는 컨텐츠가 없다면 예외가 발생합니다")
    void getEpisode404() {
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

        EpisodeGetSet episodeGetWithoutCartoon = EpisodeGetSet.builder()
                .memberSessionId(member.getId())
                .cartoonId(9999L)
                .contentEpisode(content.getEpisode())
                .build();

        EpisodeGetSet contentGetWithoutEpisodeGet = EpisodeGetSet.builder()
                .memberSessionId(member.getId())
                .cartoonId(cartoon.getId())
                .contentEpisode(9999)
                .build();

        // expected
        assertThrows(ContentNotFoundException.class,
                () -> contentService.getEpisode(episodeGetWithoutCartoon));
        assertThrows(ContentNotFoundException.class,
                () -> contentService.getEpisode(contentGetWithoutEpisodeGet));
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
                .authorSession(authorSession)
                .cartoonId(cartoon.getId())
                .contentEpisode(content.getEpisode())
                .contentUpdate(contentUpdate)
                .build();

        // when
        contentService.update(contentUpdateSet);

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
                .authorSession(authorSession)
                .cartoonId(cartoon.getId())
                .contentEpisode(9999)
                .contentUpdate(contentUpdate)
                .build();

        // expected
        assertThrows(ContentNotFoundException.class,
                () -> contentService.update(contentUpdateSet));
    }
}