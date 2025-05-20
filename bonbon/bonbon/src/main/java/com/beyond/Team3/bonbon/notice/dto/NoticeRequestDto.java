package com.beyond.Team3.bonbon.notice.dto;

import com.beyond.Team3.bonbon.common.enums.PostType;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeRequestDto {

    private String title;

    private String content;

    private PostType postType = PostType.NOTICE;

    public Notice toEntity(Headquarter headquarter) {
        return Notice.builder()
                .headquarterId(headquarter)
                .title(this.title)
                .content(this.content)
                .postType(this.postType)
                .author(headquarter.getName())
                .build();
    }
}
