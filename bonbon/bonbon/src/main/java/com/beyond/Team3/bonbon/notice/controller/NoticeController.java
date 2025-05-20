package com.beyond.Team3.bonbon.notice.controller;

import com.beyond.Team3.bonbon.common.enums.PostType;
import com.beyond.Team3.bonbon.notice.dto.NoticeRequestDto;
import com.beyond.Team3.bonbon.notice.dto.NoticeResponseDto;
import com.beyond.Team3.bonbon.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "게시판", description = "게시판")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<NoticeResponseDto> getNotice(
            Principal principal,
            @PathVariable Long noticeId
    ) {
        NoticeResponseDto noticeResponseDto = noticeService.getNotice(noticeId, principal);
        return ResponseEntity.status(HttpStatus.OK).body(noticeResponseDto);
    }

    @Operation(summary = "게시글 전체 조회")
    @GetMapping("/notice")
    public ResponseEntity<Page<NoticeResponseDto>> getAllNotices(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            Principal principal,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) PostType postType
    ) {
        Page<NoticeResponseDto> noticeResponseDto = noticeService.getAllNotice(principal, pageable, search, postType);
        return ResponseEntity.ok(noticeResponseDto);
    }

    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "게시글 등록")
    @PostMapping("/notice")
    public ResponseEntity<NoticeResponseDto> createNotice(
            Principal principal,
            @RequestBody NoticeRequestDto noticeRequestdto
    ) {
        NoticeResponseDto noticeResponseDto = noticeService.createNotice(principal, noticeRequestdto);
        return ResponseEntity.ok(noticeResponseDto);
    }

    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "게시글 수정")
    @PutMapping("/notice/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(
            @PathVariable Long noticeId,
            @RequestBody NoticeRequestDto noticeRequestdto,
            Principal principal
    ) {
        NoticeResponseDto noticeResponseDto = noticeService.updateNotice(noticeId, noticeRequestdto, principal);
        return ResponseEntity.ok(noticeResponseDto);
    }

    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/notice/{noticeId}")
    public ResponseEntity<String> deleteNotice(
            @PathVariable Long noticeId,
            Principal principal) {
        noticeService.deleteNotice(noticeId, principal);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 삭제되었습니다.");
    }

    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "문자 일괄 전송")
    @PostMapping("/notice/{noticeId}/send-sms")
    public ResponseEntity<Void> sendSmsNotice(
            @PathVariable Long noticeId,
            Principal principal) {
        noticeService.sendSmsToFranchises(noticeId, principal);
        return ResponseEntity.ok().build();
    }
}
