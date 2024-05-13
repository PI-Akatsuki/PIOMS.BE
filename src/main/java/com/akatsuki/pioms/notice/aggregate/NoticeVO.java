package com.akatsuki.pioms.notice.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeVO {

    private String noticeTitle;
    private String noticeEnrollDate;
    private String noticeContent;
    private String noticeUpdateDate;
    private String adminName;

    public NoticeVO(Notice notice) {
        this.noticeTitle = notice.getNoticeTitle();
        this.noticeContent = notice.getNoticeContent();
        this.noticeEnrollDate = notice.getNoticeEnrollDate();
        this.noticeUpdateDate = notice.getNoticeUpdateDate();
        this.adminName = notice.getAdmin().getAdminName();
    }
}
