package com.akatsuki.pioms.ask.entity;

import com.akatsuki.pioms.ask.etc.ASK_STATUS;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ask")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AskEntity {
    @Id
    @Column(name = "ask_code")
    private int askCode;

    @Column(name = "ask_content")
    private String askContent;

    @Column(name = "ask_status")
    @Enumerated(EnumType.STRING)
    private ASK_STATUS askStatus;

    @Column(name = "ask_answer")
    private String askAnswer;

    @Column(name = "ask_enroll_date")
    private LocalDateTime askEnrollDate;

    @Column(name = "ask_update_date")
    private Date askUpdateDate;

    @Column(name = "ask_comment_date")
    private Date askCommentDate;

    @Column(name = "ask_title")
    private String askTitle;

    @JoinColumn(name = "franchise_owner_code")
    @OneToOne
    private FranchiseOwnerEntity franchiseOwner;

    @JoinColumn(name = "admin_code")
    @OneToOne
    private AdminEntity admin;




}
