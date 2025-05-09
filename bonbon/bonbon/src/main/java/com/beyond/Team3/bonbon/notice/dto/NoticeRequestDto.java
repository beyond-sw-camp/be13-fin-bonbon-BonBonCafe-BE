package com.beyond.Team3.bonbon.notice.dto;

import com.beyond.Team3.bonbon.common.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeRequestDto {

    private String title;

    private String content;

    private PostType postType = PostType.NOTICE;
}
