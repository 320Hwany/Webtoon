package com.webtoon.comment.presentation;

import com.webtoon.comment.application.CommentService;
import com.webtoon.comment.dto.CommentSave;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{contentId}")
    public ResponseEntity<Void> save(@LoginForMember MemberSession memberSession,
                                     @PathVariable Long contentId,
                                     @RequestBody CommentSave commentSave) {
        commentService.save(commentSave, memberSession.getId(), contentId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
