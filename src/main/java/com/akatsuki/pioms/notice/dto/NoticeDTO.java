package com.akatsuki.pioms.notice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NoticeDTO {
    private String noticeCode;
    private String noticeTitle;
    private String noticeEnrollDate;
    private String noticeContent;
    private int adminCode;

}
