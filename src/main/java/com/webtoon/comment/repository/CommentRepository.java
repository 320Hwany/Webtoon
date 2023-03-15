package com.webtoon.comment.repository;

import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.dto.response.CommentContentResp;
import com.webtoon.comment.dto.response.CommentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepository {

    Comment save(Comment comment);

    Comment getById(Long commentId);

    List<CommentResponse> findAllForMember(Long memberId, Pageable pageable);

    List<CommentContentResp> findAllForContentNewest(Long contentId, Pageable pageable);

    List<CommentContentResp> findAllForContentLikes(Long contentId, Pageable pageable);

    long count();

    void delete(Comment comment);

    void saveAll(List<Comment> commentList);
}
