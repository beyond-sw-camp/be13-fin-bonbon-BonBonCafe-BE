package com.beyond.Team3.bonbon.headquarter.dto;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterResponseDto {

    private Long headquarterId;
    private String name;
    private String headquarterTel;
    private String roadAddress;
    private String detailAddress;
    private LocalDateTime createTime;
    private LocalDateTime modifyAt;

    public static HeadquarterResponseDto from(Headquarter headquarter) {
        return new HeadquarterResponseDto(
                headquarter.getHeadquarterId(),
                headquarter.getName(),
                headquarter.getHeadquarterTel(),
                headquarter.getRoadAddress(),
                headquarter.getDetailAddress(),
                headquarter.getCreatedAt(),
                headquarter.getModifiedAt()
        );
    }
}
