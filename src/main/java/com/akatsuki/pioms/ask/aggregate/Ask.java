package com.akatsuki.pioms.ask.aggregate;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ask")
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ask_code")
    private int askCode;

    @Column(name = "ask_content")
    private String askContent;

    @Column(name = "ask_status")
    @Enumerated(EnumType.STRING)
    private ASK_STATUS askStatus = ASK_STATUS.답변대기;

    @Column(name = "ask_answer")
    private String askAnswer;

    @Column(name = "ask_enroll_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime askEnrollDate;

    @Column(name = "ask_update_date")
    private LocalDateTime askUpdateDate;

    @Column(name = "ask_comment_date")
    private LocalDateTime askCommentDate;

    @Column(name = "ask_title")
    private String askTitle;

    @JoinColumn(name = "franchise_owner_code")
    @OneToOne
    private FranchiseOwner franchiseOwner;

    @JoinColumn(name = "admin_code")
    @OneToOne
    private Admin admin;

    public Ask() {
        this.admin = new Admin(); // 이 부분은 Admin의 기본 생성자가 필요합니다.
        this.admin.setAdminCode(1); // 관리자 ID를 1로 설정
    }

    @PrePersist
    protected void onPrePersist() {
        this.askEnrollDate = LocalDateTime.now();;
    }

    public void setAskCommentDateNow() {
        this.askCommentDate = LocalDateTime.now();
    }

    public void updateAskUpdateDate() {
        this.askUpdateDate = LocalDateTime.now();
    }
}
