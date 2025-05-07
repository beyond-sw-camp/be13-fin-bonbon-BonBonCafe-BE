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
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private Headquarter headquarterId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private PostType postType = PostType.NOTICE;

    private String author;

    @Column(nullable = false)
    private boolean isSent = false;

    public void markAsSent() {
        this.isSent = true;
    }

    public static Notice createNotice(Headquarter headquarter, NoticeRequestDto noticeRequestdto) {

        return Notice.builder()
                .headquarterId(headquarter)
                .title(noticeRequestdto.getTitle())
                .content(noticeRequestdto.getContent())
                .postType(noticeRequestdto.getPostType())
                .author(noticeRequestdto.getAuthor())
                .build();
    }

    public void updateNotice(NoticeRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.postType = dto.getPostType();
        this.author = dto.getAuthor();
    }

    public boolean hasPermission(Long headquarterId) {
        return this.headquarterId != null && this.headquarterId.getHeadquarterId().equals(headquarterId);
    }
}
