package com.beyond.Team3.bonbon.notice.service;

import com.beyond.Team3.bonbon.common.enums.PostType;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.notice.dto.NoticeRequestDto;
import com.beyond.Team3.bonbon.notice.dto.NoticeResponseDto;
import com.beyond.Team3.bonbon.notice.entity.Notice;
import com.beyond.Team3.bonbon.notice.repository.NoticeRepository;
import com.beyond.Team3.bonbon.sms.CoolSmsService;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

import static com.beyond.Team3.bonbon.handler.message.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final UserRepository userRepository;
    private final CoolSmsService coolSmsService;
    private final NoticeRepository noticeRepository;
    private final FranchiseRepository franchiseRepository;
    private final HeadquarterRepository headquarterRepository;

    public NoticeResponseDto getNotice(Long noticeId, Principal principal) {

        Headquarter headquarter = getUserHeadquarter(principal);
        Notice notice = getVerifiedNotice(noticeId, headquarter.getHeadquarterId());
        return NoticeResponseDto.from(notice);
    }

    public Page<NoticeResponseDto> getAllNotice(Principal principal, Pageable pageable, String search, PostType postType) {
        Headquarter headquarter = getUserHeadquarter(principal);

        Page<Notice> noticePage = noticeRepository.findAllNotice(headquarter.getHeadquarterId(), pageable, search, postType);
        return noticePage.map(NoticeResponseDto::from);
    }

    @Transactional
    public NoticeResponseDto createNotice(Principal principal, NoticeRequestDto noticeRequestdto) {
        Headquarter headquarter = getUserHeadquarter(principal);

        Notice notice = noticeRequestdto.toEntity(headquarter);
        noticeRepository.save(notice);

        return NoticeResponseDto.from(notice);
    }

    @Transactional
    public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto dto, Principal principal) {
        Headquarter headquarter = getUserHeadquarter(principal);
        Notice notice = getVerifiedNotice(noticeId, headquarter.getHeadquarterId());
        notice.updateNotice(dto);
        return NoticeResponseDto.from(notice);
    }

    @Transactional
    public void deleteNotice(Long noticeId, Principal principal) {
        Headquarter headquarter = getUserHeadquarter(principal);
        getVerifiedNotice(noticeId, headquarter.getHeadquarterId());
        noticeRepository.deleteById(noticeId);
    }

    @Transactional
    public void sendSmsToFranchises(Long noticeId, Principal principal) {
        Headquarter headquarter = getUserHeadquarter(principal);
        Notice notice = getVerifiedNotice(noticeId, headquarter.getHeadquarterId());

        List<String> phoneNumbers = franchiseRepository
                .findByHeadquarterId_HeadquarterId(notice.getHeadquarterId().getHeadquarterId())
                .stream()
                .map(f -> f.getFranchiseTel().replaceAll("[^0-9]", ""))
                .distinct()
                .toList();

        String prefix = switch (notice.getPostType()) {
            case NOTICE -> "[공지] ";
            case EVENT -> "[이벤트] ";
        };
        coolSmsService.sendBulkMessage(phoneNumbers, prefix + notice.getTitle());

        noticeRepository.markAsSentOnly(noticeId);
    }

    private Headquarter getUserHeadquarter(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return headquarterRepository.findById(user.getHeadquarterId().getHeadquarterId())
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
