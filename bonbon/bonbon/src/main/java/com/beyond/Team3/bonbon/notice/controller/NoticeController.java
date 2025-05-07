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
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시판", description = "게시판")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/notice/{noticeId}/{headquarterId}")
    public ResponseEntity<NoticeResponseDto> getNotice(
            @PathVariable Long noticeId,
            @PathVariable Long headquarterId
    ) {
        NoticeResponseDto noticeResponseDto = noticeService.getNotice(noticeId, headquarterId);
        return ResponseEntity.status(HttpStatus.OK).body(noticeResponseDto);
    }

    @Operation(summary = "게시글 전체 조회")
    @GetMapping("/notice/{headquarterId}")
    public ResponseEntity<Page<NoticeResponseDto>> getAllNotices(
            @PathVariable Long headquarterId,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) PostType postType
    ) {
        Page<NoticeResponseDto> noticeResponseDto = noticeService.getAllNotice(headquarterId, pageable, search, postType);
        return ResponseEntity.ok(noticeResponseDto);
    }

    @Operation(summary = "게시글 등록")
    @PostMapping("/notice/{headquarterId}")
    public ResponseEntity<NoticeResponseDto> createNotice(
            @PathVariable Long headquarterId,
            @RequestBody NoticeRequestDto noticeRequestdto
    ) {
        NoticeResponseDto noticeResponseDto = noticeService.createNotice(headquarterId, noticeRequestdto);
        return ResponseEntity.ok(noticeResponseDto);
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/notice/{noticeId}/{headquarterId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(
            @PathVariable Long noticeId,
            @RequestBody NoticeRequestDto noticeRequestdto,
            @PathVariable Long headquarterId
    ) {
        NoticeResponseDto noticeResponseDto = noticeService.updateNotice(noticeId, noticeRequestdto, headquarterId);
        return ResponseEntity.ok(noticeResponseDto);
    }

    @Operation
    @DeleteMapping("/notice/{noticeId}/{headquarterId}")
    public ResponseEntity<String> deleteNotice(
            @PathVariable Long noticeId,
            @PathVariable Long headquarterId) {
        noticeService.deleteNotice(noticeId, headquarterId);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 삭제되었습니다.");
    }
}
