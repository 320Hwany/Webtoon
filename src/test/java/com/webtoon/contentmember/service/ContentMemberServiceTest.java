package com.webtoon.contentmember.service;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.contentmember.dto.requeset.ContentMemberSave;
import com.webtoon.member.domain.Member;
import com.webtoon.util.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
class ContentMemberServiceTest extends ServiceTest {

    @Autowired
    private ContentMemberService contentMemberService;

    @Test
    @DisplayName("회원 - 만화 내용 연결 테이블에 회원과 만화내용을 저장합니다")
    void save() {
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
}