package com.beyond.Team3.bonbon.notice.repository;

import com.beyond.Team3.bonbon.common.enums.PostType;
import com.beyond.Team3.bonbon.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    Page<Notice> findAllNotice(Long headquarterId, Pageable pageable, String search, PostType postType);

}
