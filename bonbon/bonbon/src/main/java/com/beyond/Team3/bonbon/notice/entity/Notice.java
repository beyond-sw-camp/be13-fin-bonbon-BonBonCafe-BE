package com.beyond.Team3.bonbon.notice.entity;

import com.beyond.Team3.bonbon.common.base.EntityDate;
import com.beyond.Team3.bonbon.common.enums.PostType;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.notice.dto.NoticeRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@Getter
@Entity
@DynamicUpdate // 업데이터 할 때 변경된 필드만
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notice")
public class Notice extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private Headquarter headquarterId;

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT", name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType = PostType.NOTICE;

    @Column(name = "author")
    private String author;

    @Column(nullable = false, name = "is_sent")
    private boolean isSent = false;

    public void markAsSent() {
        this.isSent = true;
    }

    public void updateNotice(NoticeRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.postType = dto.getPostType();
    }

    public boolean hasPermission(Long headquarterId) {
        return this.headquarterId != null && this.headquarterId.getHeadquarterId().equals(headquarterId);
    }
}
