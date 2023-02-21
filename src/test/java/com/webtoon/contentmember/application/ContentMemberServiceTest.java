package com.webtoon.contentmember.application;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.contentmember.dto.requeset.ContentMemberSave;
import com.webtoon.member.domain.Member;
import com.webtoon.member.exception.MemberNotFoundException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContentMemberServiceTest extends ServiceTest {

    @Autowired
    private ContentMemberService contentMemberService;

    @Test
    @DisplayName("회원 - 만화 내용 연결 테이블에 회원과 만화내용을 저장합니다")
    void save200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();

        ContentMemberSave contentMemberSave = ContentMemberSave.builder()
                .contentId(content.getId())
                .memberId(member.getId())
                .build();

        // when
        contentMemberService.save(contentMemberSave);

        // then
        assertThat(contentMemberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원, 만화 정보가 없다면 예외가 발생합니다")
    void save404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        Member member = saveMemberInRepository();

        ContentMemberSave contentMemberSaveWithoutContent = ContentMemberSave.builder()
                .contentId(9999L)
                .memberId(member.getId())
                .build();

        ContentMemberSave contentMemberSaveWithoutMember = ContentMemberSave.builder()
                .contentId(content.getId())
                .memberId(9999L)
                .build();

        ContentMemberSave contentMemberSaveWithoutBoth = ContentMemberSave.builder()
                .contentId(9999L)
                .memberId(9999L)
                .build();

        // expected
        assertThrows(ContentNotFoundException.class,
                () -> contentMemberService.save(contentMemberSaveWithoutContent));
        assertThrows(MemberNotFoundException.class,
                () -> contentMemberService.save(contentMemberSaveWithoutMember));
        assertThrows(ContentNotFoundException.class,
                () -> contentMemberService.save(contentMemberSaveWithoutBoth));
    }
}