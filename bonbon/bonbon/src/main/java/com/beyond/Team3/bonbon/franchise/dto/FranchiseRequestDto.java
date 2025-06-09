package com.beyond.Team3.bonbon.franchise.dto;

import com.beyond.Team3.bonbon.common.enums.FranchiseStatus;
import com.beyond.Team3.bonbon.common.enums.RegionName;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.region.entity.Region;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
public class FranchiseRequestDto {

    @NotBlank(message = "가맹점 이름은 필수입니다.")
    private String name;

    @NotNull(message = "지역은 필수입니다.")
    private RegionName regionName;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다. 예: 02-1234-5678")
    private String franchiseTel;

    @NotBlank(message = "도로명 주소는 필수입니다.")
    private String roadAddress;

    @NotBlank(message = "상세 주소는 필수입니다.")
    private String detailAddress;

    @NotNull(message = "개점 일자는 필수입니다.")
    @PastOrPresent(message = "개점 일자는 과거 또는 오늘이어야 합니다.")
    private LocalDate openDate;

    private String memo;

    @NotBlank(message = "매장 사진 URL은 필수입니다.")
    private String franchiseImage;

    @Min(value = 5, message = "매장 크기는 5 이상이어야 합니다.")
    private int storeSize;

    @Min(value = 0, message = "좌석 수는 0 이상이어야 합니다.")
    private int seatingCapacity;

    private boolean parkingAvailability;

    @NotNull(message = "운영 상태는 필수입니다.")
    private FranchiseStatus status;

    @NotBlank(message = "운영 시간 정보는 필수입니다.")
    private String openHours;


    public Franchise toEntity(Headquarter headquarter, Region region) {
        return Franchise.builder()
                .name(name)
                .headquarterId(headquarter)
                .regionCode(region)
                .franchiseTel(franchiseTel)
                .roadAddress(roadAddress)
                .detailAddress(detailAddress)
                .openDate(openDate)
                .franchiseImage(franchiseImage)
                .memo(memo)
                .storeSize(storeSize)
                .seatingCapacity(seatingCapacity)
                .parkingAvailability(parkingAvailability)
                .status(status)
                .openHours(openHours)
                .build();
    }





}
