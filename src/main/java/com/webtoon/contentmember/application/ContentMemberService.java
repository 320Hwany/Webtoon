package com.webtoon.contentmember.application;

import com.webtoon.content.domain.Content;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.contentmember.domain.ContentMember;
import com.webtoon.contentmember.dto.requeset.ContentMemberSave;
import com.webtoon.contentmember.repository.ContentMemberRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ContentMemberService {

    private final ContentMemberRepository contentMemberRepository;
    private final ContentRepository contentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(ContentMemberSave contentMemberSave) {
        Content content = contentRepository.getById(contentMemberSave.getContentId());
        Member member = memberRepository.getById(contentMemberSave.getMemberId());
        ContentMember contentMember = ContentMember.getFromContentAndMember(content, member);
        contentMemberRepository.save(contentMember);
    }
}
