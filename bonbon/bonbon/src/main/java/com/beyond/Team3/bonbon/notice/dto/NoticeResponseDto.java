package com.beyond.Team3.bonbon.notice.dto;

import com.beyond.Team3.bonbon.common.enums.PostType;
import com.beyond.Team3.bonbon.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NoticeResponseDto {

    private Long noticeId;
    private Long headquarterId;
    private String headquarterName;
    private String title;
    private String content;
    private PostType postType = PostType.NOTICE;
    private String author;
    private LocalDateTime createTime;
    private LocalDateTime modifyAt;
    private boolean isSent;

    public static NoticeResponseDto from(Notice notice) {
        return new NoticeResponseDto(
                notice.getNoticeId(),
                notice.getHeadquarterId().getHeadquarterId(),
                notice.getHeadquarterId().getName(),
                notice.getTitle(),
                notice.getContent(),
                notice.getPostType(),
                notice.getAuthor(),
                notice.getCreatedAt(),
                notice.getModifiedAt(),
                notice.isSent()
        );
    }
}
