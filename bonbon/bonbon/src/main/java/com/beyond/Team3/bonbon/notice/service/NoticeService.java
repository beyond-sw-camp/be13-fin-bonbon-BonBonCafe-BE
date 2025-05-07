package com.beyond.Team3.bonbon.notice.service;

import com.beyond.Team3.bonbon.common.enums.PostType;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.notice.dto.NoticeRequestDto;
import com.beyond.Team3.bonbon.notice.dto.NoticeResponseDto;
import com.beyond.Team3.bonbon.notice.entity.Notice;
import com.beyond.Team3.bonbon.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final HeadquarterRepository headquarterRepository;
    private final NoticeRepository noticeRepository;

    public NoticeResponseDto getNotice(Long noticeId, Long headquarterId) {
        Notice notice = getVerifiedNotice(noticeId, headquarterId);
        return NoticeResponseDto.from(notice);
    }

    public Page<NoticeResponseDto> getAllNotice(Long headquarterId, Pageable pageable, String search, PostType postType) {
        Page<Notice> noticePage = noticeRepository.findAllNotice(headquarterId, pageable, search, postType);
        return noticePage.map(NoticeResponseDto::from);
    }

    @Transactional
    public NoticeResponseDto createNotice(Long headquarterId, NoticeRequestDto noticeRequestdto) {
        Headquarter headquarter = getVerifiedHeadquarter(headquarterId);
        Notice notice = Notice.createNotice(headquarter, noticeRequestdto);
        noticeRepository.save(notice);
        return NoticeResponseDto.from(notice);
    }

    @Transactional
    public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto dto, Long headquarterId) {
        Notice notice = getVerifiedNotice(noticeId, headquarterId);
        notice.updateNotice(dto);
        return NoticeResponseDto.from(notice);
    }

    @Transactional
    public void deleteNotice(Long noticeId, Long headquarterId) {
        getVerifiedNotice(noticeId, headquarterId);
        noticeRepository.deleteById(noticeId);
    }

    // 중복 제거 메서드
    private Headquarter getVerifiedHeadquarter(Long headquarterId) {
        return headquarterRepository.findById(headquarterId)
                .orElseThrow(() -> new IllegalArgumentException("해당 본사가 없습니다"));
    }

    private Notice getVerifiedNotice(Long noticeId, Long headquarterId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다"));
        if (!notice.hasPermission(headquarterId)) {
            throw new IllegalArgumentException("다른 본사의 게시글입니다.");
        }
        return notice;
    }
}
