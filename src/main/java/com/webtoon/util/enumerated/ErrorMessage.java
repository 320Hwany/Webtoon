package com.webtoon.util.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    AUTHOR_DUPLICATION("이미 가입된 작가 회원이 있습니다"),
    AUTHOR_NOT_FOUND("작가를 찾을 수 없습니다"),
    AUTHOR_NOT_MATCH("이메일 또는 비밀번호가 일치하지 않습니다"),
    AUTHOR_UNAUTHORIZED("작가님 로그인 후 이용해주세요"),
    CARTOON_FORBIDDEN("해당 만화에 접근 권한이 없습니다"),
    CARTOON_NOT_FOUND("만화를 찾을 수 없습니다"),
    CARTOON_MEMBER_NOT_FOUND("만화 - 회원 연결 정보를 찾을 수 없습니다"),
    COMMENT_FORBIDDEN("해당 댓글에 접근 권한이 없습니다"),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다"),
    CONTENT_NOT_FOUND("컨텐츠를 찾을 수 없습니다"),
    CONTENT_IMG_INFO_NOT_FOUND("컨텐츠 이미지를 찾을 수 없습니다"),
    GET_IMG_BAD_REQUEST("해당 이미지를 가져올 수 없습니다"),
    IMG_UPLOAD_BAD_REQUEST("해당 이미지를 업로드 할 수 없습니다"),
    MEDIA_TYPE_BAD_REQUEST("MediaType을 알 수 없습니다"),
    LACK_OF_COIN_BAD_REQUEST("코인이 부족합니다"),
    MEMBER_DUPLICATION("이미 가입된 회원이 있습니다"),
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다"),
    MEMBER_UNAUTHORIZED("로그인 후 이용해주세요"),
    ENUM_TYPE_VALIDATION("잘못된 타입 요청입니다"),
    DAY_OF_THE_WEEK_BAD_REQUEST("요일을 잘못 입력하였습니다"),
    PROGRESS_BAD_REQUEST("현재 진행 상황을 잘못 입력하였습니다"),
    GENRE_BAD_REQUEST("장르를 잘못 입력하였습니다"),
    GENDER_BAD_REQUEST("성별을 잘못 입력하였습니다"),
    VALID_BAD_REQUEST("잘못된 요청입니다");

    private String value;
}
