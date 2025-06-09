package com.beyond.Team3.bonbon.franchise.dto;

import com.beyond.Team3.bonbon.common.enums.FranchiseStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseUpdateRequestDto {

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다.")
    private String franchiseTel;

    private String franchiseImage;

    private String memo;

    @Min(value = 5, message = "매장 크기는 5 이상이어야 합니다.")
    private Integer storeSize;  // int → Integer (null 허용)

    @Min(value = 0, message = "좌석 수는 0 이상이어야 합니다.")
    private Integer seatingCapacity;

    private Boolean parkingAvailability;

    private FranchiseStatus status;

    private String openHours;
}
