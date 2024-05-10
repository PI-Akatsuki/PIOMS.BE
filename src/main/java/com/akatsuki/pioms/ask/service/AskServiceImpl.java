package com.akatsuki.pioms.ask.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.ask.dto.AskCreateDTO;
import com.akatsuki.pioms.ask.dto.AskUpdateDTO;

import com.akatsuki.pioms.ask.entity.AskEntity;

import com.akatsuki.pioms.ask.etc.ASK_STATUS;

import com.akatsuki.pioms.ask.repository.AskRepository;
import com.akatsuki.pioms.ask.vo.AskListVO;
import com.akatsuki.pioms.ask.vo.AskVO;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.akatsuki.pioms.ask.etc.ASK_STATUS.답변완료;

@Service
public class AskServiceImpl implements AskService{

    AskRepository askRepository;
    FranchiseOwnerRepository franchiseOwnerRepository;
    AdminRepository adminRepository;

    @Autowired
    public AskServiceImpl(AskRepository askRepository,FranchiseOwnerRepository franchiseOwnerRepository,AdminRepository adminRepository){
        this.askRepository = askRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.adminRepository = adminRepository;

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

    @Override
    public AskVO getAskDetails(int askCode) throws EntityNotFoundException {
        AskEntity askEntity = askRepository.findById(askCode)
                .orElseThrow(() -> new EntityNotFoundException("Ask not found with id: " + askCode));

        return new AskVO(askEntity);
    }

    public AskListVO getWaitingForReplyAsks() {
        List<AskEntity> askList = askRepository.findAllByStatusWaitingForReply();
        return convertToAskListVO(askList);
    }

    @Override
    public AskListVO getAsksByFranchiseOwnerId(Integer franchiseOwnerId) {
        List<AskEntity> askList = askRepository.findByFranchiseOwner_FranchiseOwnerCode(franchiseOwnerId);
        return convertToAskListVO(askList);
    }

    private AskListVO convertToAskListVO(List<AskEntity> askList) {
        List<AskVO> askVOList = new ArrayList<>();
        askList.forEach(ask -> askVOList.add(new AskVO(ask)));
        return new AskListVO(askVOList);
    }

    public AskVO answerAsk(Integer askId, String answer) {
        Optional<AskEntity> ask = askRepository.findById(askId);
        if (ask.isPresent()) {
            AskEntity askEntity = ask.get();
            askEntity.setAskAnswer(answer);
            askEntity.setAskStatus(답변완료);
            askEntity.setAskCommentDateNow();  // 답변 등록 시각 설정
            askRepository.save(askEntity);
            return new AskVO(askEntity);
        } else {
            throw new RuntimeException("Ask not found with id: " + askId);
        }
    }




    @Override
    public AskVO createAsk(AskCreateDTO askDTO) {
        AskEntity askEntity = new AskEntity();
        askEntity.setAskTitle(askDTO.getTitle());
        askEntity.setAskContent(askDTO.getContent());

        FranchiseOwner owner = franchiseOwnerRepository.findById(askDTO.getFranchiseOwnerCode())
                .orElseThrow(() -> new RuntimeException("Franchise owner not found"));
        askEntity.setFranchiseOwner(owner);

        // Admin 정보 가져오기
        Admin admin = adminRepository.findById(1)  // 예: 관리자 ID가 1인 경우
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        askEntity.setAdmin(admin);
        askRepository.save(askEntity);
        return new AskVO(askEntity);
    }

    public AskEntity updateAsk(int askCode, AskUpdateDTO askUpdateDTO) throws Exception {
        AskEntity askEntity = askRepository.findById(askCode)
                .orElseThrow(() -> new RuntimeException("Ask not found with id: " + askCode));

        if (askEntity.getAskStatus() != ASK_STATUS.답변대기) {
            throw new Exception("답변완료의 경우 수정할 수 없습니다.");
        }

        askEntity.setAskTitle(askUpdateDTO.getTitle());
        askEntity.setAskContent(askUpdateDTO.getContent());
        askEntity.updateAskUpdateDate();  // Ensure this method is implemented to only update ask_update_date
        return askRepository.save(askEntity);
    }



}
