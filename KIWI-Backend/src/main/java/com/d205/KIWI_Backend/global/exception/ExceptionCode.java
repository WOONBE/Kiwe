package com.d205.KIWI_Backend.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),

    NOT_FOUND_MENU(1001, "해당 메뉴가 존재하지 않습니다."),


    INVALID_CATEGORY(1002, "유효하지 않은 카테고리 입니다."),

    NOT_FOUND_MEMBER_ID(1010, "요청한 ID에 해당하는 멤버가 존재하지 않습니다."),


    NOT_FOUND_THEME_ID(1003, "요청한 ID에 해당하는 테마가 존재하지 않습니다."),

//    NOT_FOUND_MEMBER_ID(1004, "요청한 ID에 해당하는 유저가 존재하지 않습니다."),
    NOT_FOUND_EMAIL(1005, "요청한 ID에 해당하는 이메일이 존재하지 않습니다."),
    NOT_FOUND_ARTWORK_ID(1006, "요청한 ID에 해당하는 미술품이 존재하지 않습니다."),

    NOT_FOUND_KIOSK_ID(1007, "요청한 ID에 해당하는 키오스크가 없습니다"),
    NOT_CORRECT_PASSWORD(1008, "입력한 비밀번호가 일치하지 않습니다."),
    NOT_FOUND_CURRENCY_DATA(1009, "요청한 날짜에 해당하는 환율 정보가 존재하지 않습니다."),

    NOT_VALID_REFRESH_TOKEN(1011, "유효한 refresh token이 아닙니다."),

    NOT_VALID_UPDATE_KIOSK(1012, "수정가능한 키오스크가 아닙니다."),

    DUPLICATED_MEMBER_NICKNAME(1013, "중복된 닉네임입니다."),

    NOT_FOUND_THEME_WITH_GALLERY(1014, "요청한 갤러리에 해당 테마가 존재하지 않습니다."),

    NOT_SUBSCRIBED_GALLERY(1015, "구독한 미술관이 아닙니다"),

    ALREADY_DELETED_TRIP_ITEM(2001, "이미 삭제된 여행 아이템입니다."),
    ALREADY_DELETED_DATE(2002, "이미 삭제된 날짜입니다."),
    ALREADY_SUBSCRIBED_GALLERY(2002, "이미 구독된 미술관입니다."),

    INVALID_ARTWORK_TYPE(3001, "유효하지 않은 미술품 형식입니다"),
    INVALID_GENRE(3002, "유효하지 않은 장르입니다."),
    INVALID_NULL_PLACE(3003, "아이템의 장소 정보가 필요합니다."),
    INVALID_NOT_NULL_PLACE(3004, "아이템의 장소 정보가 불필요합니다."),
    INVALID_IS_PLACE_UPDATED_WHEN_NON_SPOT(3005, "아이템이 기타일 때, 장소를 업데이트할 수 없습니다."),
    INVALID_CURRENCY_DATE_WHEN_WEEKEND(3006, "주말의 공공 환율 api를 조회할 수 없습니다."),
    INVALID_DATE_ALREADY_EXIST(3007, "요청한 날짜의 환율 정보는 이미 존재합니다."),
    INVALID_EXPENSE_OVER_MAX(3008, "금액이 1억원보다 클 수 없습니다."),
    INVALID_EXPENSE_UNDER_MIN(3009, "금액이 0원보다 작을 수 없습니다."),

    INVALID_ORDERED_ITEM_IDS(4001, "날짜에 속한 모든 여행 아이템들의 ID가 필요합니다."),

    EXCEED_IMAGE_CAPACITY(5001, "업로드 가능한 이미지 용량을 초과했습니다."),
    NULL_IMAGE(5002, "업로드한 이미지 파일이 NULL입니다."),
    EMPTY_IMAGE_LIST(5003, "최소 한 장 이상의 이미지를 업로드해야합니다."),
    EXCEED_IMAGE_LIST_SIZE(5004, "업로드 가능한 이미지 개수를 초과했습니다."),
    INVALID_IMAGE_URL(5005, "요청한 이미지 URL의 형식이 잘못되었습니다."),
    INVALID_IMAGE_PATH(5101, "이미지를 저장할 경로가 올바르지 않습니다."),
    FAIL_IMAGE_NAME_HASH(5102, "이미지 이름을 해싱하는 데 실패했습니다."),
    INVALID_IMAGE(5103, "올바르지 않은 이미지 파일입니다."),

    NOT_ASSOCIATE_DAYLOG_WITH_TRIP(6001, "요청한 DayLog와 Trip은 연관관계가 아닙니다."),

    INVALID_UPDATE(7001, "요청한 변경이 불가능 합니다."),
    INVALID_SHARE_CODE(7002, "공유가 허용되지 않은 코드입니다."),
    FAIL_SHARE_CODE_HASH(7101, "공유 코드를 해싱하는 데 실패했습니다."),

    //    INVALID_USER_NAME(8001, "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(8002, "비밀번호가 일치하지 않습니다."),
//    NULL_ADMIN_AUTHORITY(8101, "잘못된 관리자 권한입니다."),
//    DUPLICATED_ADMIN_USERNAME(8102, "중복된 사용자 이름입니다."),
//    NOT_FOUND_ADMIN_ID(8103, "요청한 ID에 해당하는 관리자를 찾을 수 없습니다."),
//    INVALID_CURRENT_PASSWORD(8104, "현재 사용중인 비밀번호가 일치하지 않습니다."),
//    INVALID_ADMIN_AUTHORITY(8201, "해당 관리자 기능에 대한 접근 권한이 없습니다."),
//    DUPLICATED_CITY_NAME(8301, "중복된 나라, 도시 이름입니다."),
//    NOT_FOUND_CITY(8302, "요청한 ID에 해당하는 도시를 찾을 수 없습니다."),
//    DUPLICATED_CATEGORY_ID(8311, "중복된 카테고리 아이디입니다."),
//    DUPLICATED_CATEGORY_ENG_NAME(8312, "중복된 카테고리 영어 이름입니다."),
//    NOT_FOUND_CATEGORY(8314, "요청한 ID에 해당하는 카테고리를 찾을 수 없습니다."),
    EMPTY_FILE_EXCEPTION(8001, "존재하지 않는 파일입니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(8002, "S3에 이미지 업로드 중 입출력 오류가 발생했습니다."),
    NO_FILE_EXTENTION(8003, "파일 확장자가 없습니다."),
    INVALID_FILE_EXTENTION(8004, "유효하지 않은 파일 확장자입니다."),
    PUT_OBJECT_EXCEPTION(8005, "S3 put object 기능에서 오류가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(8006, "S3에서 이미지 삭제 중 입출력 오류가 발생했습니다."),



    INVALID_AUTHORIZATION_CODE(9001, "유효하지 않은 인증 코드입니다."),
    NOT_SUPPORTED_OAUTH_SERVICE(9002, "해당 OAuth 서비스는 제공하지 않습니다."),
    FAIL_TO_CONVERT_URL_PARAMETER(9003, "Url Parameter 변환 중 오류가 발생했습니다."),
    INVALID_REFRESH_TOKEN(9101, "올바르지 않은 형식의 RefreshToken입니다."),
    INVALID_ACCESS_TOKEN(9102, "올바르지 않은 형식의 AccessToken입니다."),
    EXPIRED_PERIOD_REFRESH_TOKEN(9103, "기한이 만료된 RefreshToken입니다."),
    EXPIRED_PERIOD_ACCESS_TOKEN(9104, "기한이 만료된 AccessToken입니다."),
    FAIL_TO_VALIDATE_TOKEN(9105, "토큰 유효성 검사 중 오류가 발생했습니다."),
    NOT_FOUND_REFRESH_TOKEN(9106, "refresh-token에 해당하는 쿠키 정보가 없습니다."),
    INVALID_AUTHORITY(9201, "해당 요청에 대한 접근 권한이 없습니다."),

    INTERNAL_SEVER_ERROR(9999, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요.");

    private final int code;
    private final String message;
}
