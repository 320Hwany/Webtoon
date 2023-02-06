package com.webtoon.contentmember.controller;

import com.webtoon.contentmember.dto.requeset.ContentMemberSave;
import com.webtoon.contentmember.service.ContentMemberService;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ContentMemberController {

    private final ContentMemberService contentMemberService;

    @PostMapping("/read/{contentId}")
    public ResponseEntity<Void> memberReadContent(@LoginForMember MemberSession memberSession,
                                            @PathVariable Long contentId) {

        ContentMemberSave contentMemberSave =
                ContentMemberSave.getFromContentIdAndMemberId(contentId, memberSession.getId());
        contentMemberService.save(contentMemberSave);
        return ResponseEntity.ok().build();
    }
}
