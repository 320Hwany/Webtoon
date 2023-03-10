package com.webtoon.comment.repository;

import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.dto.response.CommentResponse;

import java.util.List;

public interface CommentRepository {

    Comment save(Comment comment);

    Comment getById(Long commentId);

    List<CommentResponse> findAllForMember(Long memberId);

    long count();

    void delete(Comment comment);

    void saveAll(List<Comment> commentList);
}
