package com.beyond.Team3.bonbon.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RegionName {
    // 서울특별시
    SEOUL_JONGNOGU    ("서울특별시 종로구"),   // 11110
    SEOUL_JUNGGU      ("서울특별시 중구"),     // 11140
    SEOUL_YONGSANGU   ("서울특별시 용산구"),   // 11170
    SEOUL_GANGNAMGU   ("서울특별시 강남구"),   // 11680
    SEOUL_GANGDONGGU  ("서울특별시 강동구"),   // 11740
    SEOUL_SEONGDONGGU ("서울특별시 성동구"),   // 11200
    SEOUL_GWANGJINGU  ("서울특별시 광진구"),   // 11215
    SEOUL_DONGDAEMUNGU("서울특별시 동대문구"), // 11230
    SEOUL_JUNGRANGGU  ("서울특별시 중랑구"),   // 11260
    SEOUL_SEONGBUKGU  ("서울특별시 성북구"),   // 11290

    // 경기도
    GYEONGGI_SUWONSI     ("경기도 수원시"),     // 41010
    GYEONGGI_SEONGNAMSI  ("경기도 성남시"),     // 41020
    GYEONGGI_UIJEONGBUSI ("경기도 의정부시"),   // 41030
    GYEONGGI_ANNYANGSI   ("경기도 안양시"),     // 41040
    GYEONGGI_BUCHEONSI   ("경기도 부천시"),     // 41050
    GYEONGGI_ANSANSI     ("경기도 안산시"),     // 41190
    GYEONGGI_GOYANGSI    ("경기도 고양시"),     // 41210

    // 인천광역시
    INCHEON_BUPYEONGGU("인천광역시 부평구"), // 28160
    INCHEON_GYUYANGGU ("인천광역시 계양구"), // 28170
    INCHEON_SEOGU     ("인천광역시 서구"),   // 28200
    INCHEON_NAMDONGGU ("인천광역시 남동구"), // 28230
    INCHEON_YEONSUGU  ("인천광역시 연수구"), // 28245

    // 충청북도
    CHUNGBUK_CHEONGJUSI  ("충청북도 청주시"),  // 43170
    CHUNGBUK_CHUNGJUSI   ("충청북도 충주시"),  // 43190
    CHUNGBUK_BOEUNGUN    ("충청북도 보은군"),  // 43130
    CHUNGBUK_DANYANGGUN  ("충청북도 단양군"),  // 43150
    CHUNGBUK_YEONGDONGGUN("충청북도 영동군"),  // 43110

    // 충청남도
    CHUNGNAM_CHEONANSI  ("충청남도 천안시"),  // 44010
    CHUNGNAM_GONGJUSI  ("충청남도 공주시"),  // 44050
    CHUNGNAM_BORYEONGSI("충청남도 보령시"),  // 44190
    CHUNGNAM_SEOSANSI  ("충청남도 서산시"),  // 44230
    CHUNGNAM_NONSANSI  ("충청남도 논산시"),  // 44270

    // 부산광역시
    BUSAN_JUNGGU     ("부산광역시 중구"),     // 26110
    BUSAN_SEOGU      ("부산광역시 서구"),     // 26140
    BUSAN_DONGGU     ("부산광역시 동구"),     // 26170
    BUSAN_YEONGDOGU  ("부산광역시 영도구"),   // 26200
    BUSAN_BUSANJINGU("부산광역시 부산진구"),  // 26230
    BUSAN_DONGRAEGU  ("부산광역시 동래구"),   // 26260

    // 대구광역시
    DAEGU_JUNGGU    ("대구광역시 중구"),    // 27210
    DAEGU_DONGGU    ("대구광역시 동구"),    // 27230
    DAEGU_SEOGU     ("대구광역시 서구"),    // 27250
    DAEGU_NAMGU     ("대구광역시 남구"),    // 27260
    DAEGU_BUKGU     ("대구광역시 북구"),    // 27280
    DAEGU_SUSEONGGU("대구광역시 수성구"),  // 27290

    // 광주광역시
    GWANGJU_DONGGU    ("광주광역시 동구"),    // 27110
    GWANGJU_SEOGU     ("광주광역시 서구"),    // 27140
    GWANGJU_NAMGU     ("광주광역시 남구"),    // 27160
    GWANGJU_BUKGU     ("광주광역시 북구"),    // 27180
    GWANGJU_GWANSANGU("광주광역시 광산구");   // 27200

    private final String displayName;

    RegionName(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
