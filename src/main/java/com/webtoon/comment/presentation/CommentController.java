package com.webtoon.comment.presentation;

import com.webtoon.comment.application.CommentService;
import com.webtoon.comment.dto.request.CommentSave;
import com.webtoon.comment.dto.request.CommentUpdate;
import com.webtoon.comment.dto.response.CommentResponse;
import com.webtoon.comment.dto.response.CommentResult;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        commentService.validateAuthorization(commentId, memberSession.getId());
        CommentResponse commentResponse = commentService.update(commentId, commentUpdate);
        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> delete(@LoginForMember MemberSession memberSession,
                                       @PathVariable Long commentId) {
        commentService.validateAuthorization(commentId, memberSession.getId());
        commentService.delete(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comment/member")
    public ResponseEntity<CommentResult> findAllForMember(@LoginForMember MemberSession memberSession,
                                                          Pageable pageable) {
        List<CommentResponse> commentResponseList =
                commentService.findAllForMember(memberSession.getId(), pageable);
        return ResponseEntity.ok(new CommentResult(commentResponseList.size(), commentResponseList));
    }
}
