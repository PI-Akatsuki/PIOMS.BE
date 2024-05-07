package com.akatsuki.pioms.ask.service;

import com.akatsuki.pioms.ask.entity.AskEntity;
import com.akatsuki.pioms.ask.repository.AskRepository;
import com.akatsuki.pioms.ask.vo.AskListVO;
import com.akatsuki.pioms.ask.vo.AskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.akatsuki.pioms.ask.etc.ASK_STATUS.답변완료;

@Service
public class AskServiceImpl implements AskService{

    AskRepository askRepository;

    @Autowired
    public AskServiceImpl(AskRepository askRepository){
        this.askRepository = askRepository;
    }

    @Override
    public AskListVO getAllAskList() {
        List<AskEntity> askList = askRepository.findAll();
        System.out.println("askList = " + askList);
        List<AskVO> askVOList = new ArrayList<>();
        askList.forEach(ask-> {
            askVOList.add(new AskVO(ask));
        });
        return new AskListVO(askVOList);
    }

    public AskListVO getWaitingForReplyAsks() {
        List<AskEntity> askList = askRepository.findAllByStatusWaitingForReply();
        return convertToAskListVO(askList);
    }

    private AskListVO convertToAskListVO(List<AskEntity> askList) {
        List<AskVO> askVOList = new ArrayList<>();
        askList.forEach(ask -> askVOList.add(new AskVO(ask)));
        return new AskListVO(askVOList);
    }

}
