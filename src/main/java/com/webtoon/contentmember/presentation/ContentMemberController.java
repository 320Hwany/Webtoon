package com.webtoon.contentmember.presentation;

import com.webtoon.contentmember.dto.requeset.ContentMemberSave;
import com.webtoon.contentmember.application.ContentMemberService;
import com.webtoon.rest_template.RestTemplateService;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.webtoon.contentmember.dto.requeset.ContentMemberSave.*;

@RequiredArgsConstructor
@RestController
public class ContentMemberController {

    private final ContentMemberService contentMemberService;
    private final RestTemplateService restTemplateService;

    @PostMapping("/contentMember/read/{contentId}")
    public ResponseEntity<byte[]> memberReadContent(@LoginForMember MemberSession memberSession,
                                                    @PathVariable Long contentId) {

        ContentMemberSave contentMemberSave = toContentMemberSave(contentId, memberSession.getId());
        contentMemberService.save(contentMemberSave);
        return restTemplateService.getContentImg(contentId);
    }
}
