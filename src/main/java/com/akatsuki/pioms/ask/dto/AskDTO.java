package com.akatsuki.pioms.ask.dto;

import com.akatsuki.pioms.ask.entity.AdminEntity;
import com.akatsuki.pioms.ask.entity.FranchiseOwnerEntity;
import com.akatsuki.pioms.ask.etc.ASK_STATUS;

import java.time.LocalDateTime;
import java.util.Date;

public class AskDTO {

    private int askCode;
    private String askComment;
    private ASK_STATUS askCondition;
    private LocalDateTime askEnrollDate;
    private Date askUpdateDate;
    private Date askCommentDate;
    private String askTitle;
    private FranchiseOwnerEntity franchiseOwner;
    private AdminEntity admin;
}
