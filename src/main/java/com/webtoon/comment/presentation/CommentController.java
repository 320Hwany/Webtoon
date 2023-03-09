package com.webtoon.comment.presentation;

import com.webtoon.comment.application.CommentService;
import com.webtoon.comment.dto.request.CommentSave;
import com.webtoon.comment.dto.request.CommentUpdate;
import com.webtoon.comment.dto.response.CommentResponse;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponse> update(@LoginForMember MemberSession memberSession,
                                                  @PathVariable Long commentId,
                                                  @RequestBody CommentUpdate commentUpdate) {

        CommentResponse commentResponse = commentService.update(commentId, commentUpdate);
        return ResponseEntity.ok(commentResponse);
    }
}
