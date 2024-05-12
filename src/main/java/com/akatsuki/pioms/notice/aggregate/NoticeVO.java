package com.akatsuki.pioms.notice.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeVO {

    private String noticeTitle;
    private String noticeEnrollDate;
    private String noticeContent;
    private String noticeUpdateDate;

    public NoticeVO(Notice notice) {
    }
}
