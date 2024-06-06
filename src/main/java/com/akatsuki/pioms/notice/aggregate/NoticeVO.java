package com.akatsuki.pioms.notice.aggregate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeVO {

    private int noticeCode;
    private String noticeTitle;
    private String noticeContent;
    private String noticeEnrollDate;
    private String noticeUpdateDate;
    private String adminName;

    public NoticeVO(Notice notice) {
        this.noticeCode = notice.getNoticeCode();
        this.noticeTitle = notice.getNoticeTitle();
        this.noticeContent = notice.getNoticeContent();
        this.noticeEnrollDate = notice.getNoticeEnrollDate();
        this.noticeUpdateDate = notice.getNoticeUpdateDate();
        this.adminName = notice.getAdmin().getAdminName();
    }
}
