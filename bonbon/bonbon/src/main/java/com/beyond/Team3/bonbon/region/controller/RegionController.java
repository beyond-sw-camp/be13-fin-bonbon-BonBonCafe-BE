package com.beyond.Team3.bonbon.region.controller;

import com.beyond.Team3.bonbon.franchise.dto.FranchiseDto;
import com.beyond.Team3.bonbon.region.Service.RegionService;
import com.beyond.Team3.bonbon.region.dto.RegionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
@Tag(name = "지역", description = "지역 필터링 관련 기능")
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/majors")
    @Operation(summary = "시/도 목록 조회", description = "지역의 시/도 목록을 조회한다. 예: 서울특별시, 경기도")
    public ResponseEntity<List<String>> getMajors() {
        return ResponseEntity.ok(regionService.getMajors());
    }

    @GetMapping("/sub")
    @Operation(summary = "구/군 목록 조회", description = "시/도에 속한 구/군 목록을 조회한다. 예: 종로구, 중구")
    public ResponseEntity<List<RegionDto>> getSubs(@RequestParam String major) {
        return ResponseEntity.ok(regionService.getSubByMajor(major));
    }

    @GetMapping("/franchises")
    @Operation(summary = "지역별 가맹점 조회", description = "지역별 구/군에 속한 가맹점 리스트를 조회한다.")
    public ResponseEntity<List<FranchiseDto>> getByRegion(
            @RequestParam("regionCode") int regionCode) {
        return ResponseEntity.ok(regionService.findByRegionCode(regionCode));
    }

}
