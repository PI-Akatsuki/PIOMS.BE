package com.akatsuki.pioms.notice.aggregate;

import com.akatsuki.pioms.admin.aggregate.Admin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notice")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_code")
    private int noticeCode;

    @Column(name = "notice_title")
    private String noticeTitle;

    @Column(name = "notice_enroll_date")
    private String noticeEnrollDate;

    @Column(name = "notice_content")
    private String noticeContent;

    @Column(name = "notice_update_date")
    private String noticeUpdateDate;

    @JoinColumn(name = "admin_code")
    @ManyToOne
    private Admin admin;
}
