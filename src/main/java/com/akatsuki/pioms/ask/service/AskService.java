package com.akatsuki.pioms.ask.service;

import com.akatsuki.pioms.ask.dto.AskCreateDTO;
import com.akatsuki.pioms.ask.dto.AskUpdateDTO;
import com.akatsuki.pioms.ask.entity.AskEntity;
import com.akatsuki.pioms.ask.vo.AskListVO;
import com.akatsuki.pioms.ask.vo.AskVO;
import jakarta.persistence.EntityNotFoundException;

public interface AskService {
    AskListVO getAllAskList();

    AskVO getAskDetails(int askCode) throws EntityNotFoundException;

    AskListVO getWaitingForReplyAsks();
    AskVO answerAsk(Integer askId, String answer);
    AskListVO getAsksByFranchiseOwnerId(Integer franchiseOwnerId);
    AskVO createAsk(AskCreateDTO askDTO);

    AskEntity updateAsk(int askCode, AskUpdateDTO askUpdateDTO) throws Exception;
}
