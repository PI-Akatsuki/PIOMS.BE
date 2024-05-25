package com.akatsuki.pioms.ask.service;

import com.akatsuki.pioms.ask.dto.AskCreateDTO;
import com.akatsuki.pioms.ask.dto.AskDTO;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.ask.dto.AskUpdateDTO;
import com.akatsuki.pioms.ask.aggregate.Ask;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import jakarta.persistence.EntityNotFoundException;

public interface AskService {
    AskListDTO getAllAskList();

    AskDTO getAskDetails(int askCode) throws EntityNotFoundException;

    AskListDTO getWaitingForReplyAsks();
    AskDTO answerAsk(Integer askId, String answer);
    AskListDTO getAsksByFranchiseOwnerId(Integer franchiseOwnerId);
    AskDTO createAsk(AskCreateDTO askDTO);

    Ask updateAsk(int askCode, AskUpdateDTO askUpdateDTO) throws Exception;

    FranchiseOwnerDTO getFranchiseOwnerDetails(int franchiseOwnerCode);
}
