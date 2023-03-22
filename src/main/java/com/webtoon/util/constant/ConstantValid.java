package com.webtoon.util.constant;

public class ConstantValid {

    private ConstantValid() {
    }

    public static final String NICKNAME_VALID_MESSAGE = "닉네임을 입력해주세요";

    public static final String EMAIL_VALID_MESSAGE = "이메일을 입력해주세요";

    public static final String PASSWORD_VALID_MESSAGE = "영문/한글/숫자 4~20자 이내로 작성해주세요";

    public static final String PASSWORD_REGEXP = "^[a-zA-Zㄱ-ㅎ가-힣0-9]{4,20}$";

    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";

    public static final String CARTOON_TITLE_VALID_MESSAGE = "만화 제목을 입력해주세요";

    public static final String PAGE_VALID_MESSAGE = "1 이상의 페이지를 입력해주세요";

    public static final String AGE_VALID_MESSAGE = "1 이상의 나이를 입력해주세요";

    public static final String CARTOON_SIZE_VALID_MESSAGE = "한 페이지당 가져올 만화의 수를 입력해주세요";

    public static final String CONTENT_SIZE_VALID_MESSAGE = "한 페이지당 가져올 컨텐츠의 수를 입력해주세요";

    public static final String SUBTITLE_VALID_MESSAGE = "만화 부제를 입력해주세요";

    public static final String EPISODE_VALID_MESSAGE = "몇 화인지를 입력해주세요";

    public static final String REGISTRATION_DATE_VALID_MESSAGE = "등록 날짜를 입력해주세요";

    public static final String CHARGE_VALID_MESSAGE = "0코인 이상을 충전해주세요";
}
