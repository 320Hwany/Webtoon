package com.webtoon.cartoon.controller;

import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class CartoonControllerTest extends ControllerTest {

//    @Test
//    @DisplayName("만화 제목을 저장")
//    void saveBookFail() {
//        CartoonSave cartoonSave = CartoonSave.builder()
//                .title("만화 제목")
//                .build();
//
//        restDocs
//                .contentType(APPLICATION_JSON_VALUE)
//                .body(cartoonSave)
//                .when().post("/cartoon")
//                .then().log().all()
//                .assertThat()
//                .apply(document("save-cartoon",
//                        requestFields(fieldWithPath("title").description("만화 제목")),
//                        responseFields(fieldWithPath("title").description("만화 제목"))
//                ))
//                .statusCode(HttpStatus.OK.value());
//    }
}