package com.webtoon.comment.repository;

import com.webtoon.comment.domain.Comment;

public interface CommentRepository {

    Comment save(Comment comment);
}
