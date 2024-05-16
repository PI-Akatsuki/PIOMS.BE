package com.akatsuki.pioms.notice.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.notice.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class NoticeServiceTest {

    private NoticeService noticeService;
    private NoticeRepository noticeRepository;
    private Admin admin;

    static int adminCode = 1;

    @Autowired
    public NoticeServiceTest(NoticeService noticeService, NoticeRepository noticeRepository, Admin admin) {
        this.noticeService = noticeService;
        this.noticeRepository = noticeRepository;
        this.admin = admin;
    }

    @BeforeEach
    void init() {

    }

    @Test
    void 공지사항_전체_조회하기() {

    }

    @Test
    void 공지사항_상세_조회하기() {

    }

}
