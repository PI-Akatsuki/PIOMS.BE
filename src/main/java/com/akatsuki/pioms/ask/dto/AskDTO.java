package com.akatsuki.pioms.ask.dto;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.ask.aggregate.ASK_STATUS;
import com.akatsuki.pioms.ask.aggregate.Ask;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AskDTO {

    private int askCode;
    private String askContent;
    private ASK_STATUS askStatus;
    private String askAnswer;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime askEnrollDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime askUpdateDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime askCommentDate;
    private String askTitle;
    private int franchiseOwnerCode;
    private String franchiseOwnerName;
    private String franchiseName;
    private int adminCode;

    public AskDTO(Ask ask) {
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
        if (ask.getFranchiseOwner().getFranchise()!=null)
            this.franchiseName = ask.getFranchiseOwner().getFranchise().getFranchiseName();
        this.adminCode = ask.getAdmin().getAdminCode();
    }
}
