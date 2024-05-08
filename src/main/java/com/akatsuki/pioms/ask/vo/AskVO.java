package com.akatsuki.pioms.ask.vo;


import com.akatsuki.pioms.ask.entity.AskEntity;
import com.akatsuki.pioms.ask.etc.ASK_STATUS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AskVO {
    private int askCode;
    private String askContent;
    private ASK_STATUS askStatus;
    private String askAnswer;
    private LocalDateTime askEnrollDate;
    private LocalDateTime askUpdateDate;
    private LocalDateTime askCommentDate;
    private String askTitle;

    private int franchiseOwnerCode;
    private String franchiseOwnerName;

    private int adminCode;
    private String adminName;

    public AskVO(AskEntity ask) {
        this.askCode= ask.getAskCode();
        this.askContent = ask.getAskContent();
        this.askStatus = ask.getAskStatus();
        this.askAnswer = ask.getAskAnswer();
        this.askEnrollDate = ask.getAskEnrollDate();
        this.askUpdateDate = ask.getAskUpdateDate();
        this.askCommentDate = ask.getAskCommentDate();
        this.askTitle = ask.getAskTitle();
        this.franchiseOwnerCode = ask.getFranchiseOwner().getFranchiseOwnerCode();
        this.franchiseOwnerName = ask.getFranchiseOwner().getFranchiseOwnerName();
        this.adminCode = ask.getAdmin().getAdminCode();
        this.adminName = ask.getAdmin().getAdminName();
    }
}

