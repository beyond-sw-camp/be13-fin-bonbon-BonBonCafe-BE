package com.beyond.Team3.bonbon.notice.repository;

import com.beyond.Team3.bonbon.common.enums.PostType;
import com.beyond.Team3.bonbon.notice.entity.Notice;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
    Page<Notice> findAllNotice(Long headquarterId, Pageable pageable, String search, PostType postType);

    @Modifying
    @Query("UPDATE Notice n SET n.isSent = true WHERE n.noticeId = :noticeId")
    void markAsSentOnly(@Param("noticeId") Long noticeId);
}
