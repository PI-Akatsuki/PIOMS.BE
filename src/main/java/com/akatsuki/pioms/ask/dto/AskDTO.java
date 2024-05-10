package com.akatsuki.pioms.ask.dto;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.ask.etc.ASK_STATUS;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;

import java.time.LocalDateTime;

public class AskDTO {

    private int askCode;
    private String askComment;
    private ASK_STATUS askCondition;
    private LocalDateTime askEnrollDate;
    private LocalDateTime askUpdateDate;
    private LocalDateTime askCommentDate;
    private String askTitle;
    private FranchiseOwner franchiseOwner;
    private Admin admin;
}
