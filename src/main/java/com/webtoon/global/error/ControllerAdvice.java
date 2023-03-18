package com.webtoon.global.error;

import com.webtoon.cartoon.exception.CartoonEnumTypeException;
import com.webtoon.contentImgInfo.exception.GetImgException;
import com.webtoon.contentImgInfo.exception.ImgUploadException;
import com.webtoon.member.exception.LackOfCoinException;
import com.webtoon.member.exception.MemberEnumTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import static com.webtoon.util.constant.ConstantCommon.*;
import static com.webtoon.util.enumerated.ErrorMessage.VALID_BAD_REQUEST;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(BAD_REQUEST)
                .message(VALID_BAD_REQUEST.getValue())
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotMatchException.class)
    public ResponseEntity<ErrorResponse> notMatchException(NotMatchException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ErrorResponse> duplicationException(DuplicationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> unauthorizedException(UnauthorizedException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbiddenException(ForbiddenException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(CartoonEnumTypeException.class)
    public ResponseEntity<ErrorResponse> enumTypeValidException(CartoonEnumTypeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        errorResponse.addValidation(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MemberEnumTypeException.class)
    public ResponseEntity<ErrorResponse> enumTypeValidException(MemberEnumTypeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        errorResponse.addValidation(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(LackOfCoinException.class)
    public ResponseEntity<ErrorResponse> lackOfCoinException(LackOfCoinException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ImgUploadException.class)
    public ResponseEntity<ErrorResponse> imgUploadException(ImgUploadException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(GetImgException.class)
    public ResponseEntity<ErrorResponse> getImgException(GetImgException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
