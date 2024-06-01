package com.akatsuki.pioms.ask.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.ask.dto.AskCreateDTO;
import com.akatsuki.pioms.ask.dto.AskDTO;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.ask.dto.AskUpdateDTO;
import com.akatsuki.pioms.ask.aggregate.Ask;
import com.akatsuki.pioms.ask.aggregate.ASK_STATUS;
import com.akatsuki.pioms.ask.repository.AskRepository;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.akatsuki.pioms.ask.aggregate.ASK_STATUS.답변완료;

@Service
public class AskServiceImpl implements AskService{

    private final AskRepository askRepository;
    private final FranchiseOwnerRepository franchiseOwnerRepository;
    private final AdminRepository adminRepository;
    private final LogService logService;

    @Autowired
    public AskServiceImpl(AskRepository askRepository,FranchiseOwnerRepository franchiseOwnerRepository,AdminRepository adminRepository,LogService logService){
        this.askRepository = askRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.adminRepository = adminRepository;
        this.logService = logService;
    }
    @Override
    public AskListDTO getAllAskList() {
        List<Ask> askList = askRepository.findAllOrderByEnrollDateDesc();
        List<AskDTO> askDTOList = askList.stream()
                .map(AskDTO::new)
                .collect(Collectors.toList());
        return new AskListDTO(askDTOList);
    }
    @Override
    public AskDTO getAskDetails(int askCode) throws EntityNotFoundException {
        Ask ask = askRepository.findById(askCode)
                .orElseThrow(() -> new EntityNotFoundException("Ask not found with id: " + askCode));

        return new AskDTO(ask);
    }

    @Override
    public AskListDTO getWaitingForReplyAsks() {
        List<Ask> askList = askRepository.findAllByStatusWaitingForReply();
        return convertToAskListDTO(askList);
    }

    @Override
    public AskListDTO getAsksByFranchiseOwnerId(Integer franchiseOwnerId) {
        List<Ask> askList = askRepository.findByFranchiseOwner_FranchiseOwnerCodeOrderByAskEnrollDateDesc(franchiseOwnerId);
        return convertToAskListDTO(askList);
    }

    private AskListDTO convertToAskListDTO(List<Ask> askList) {
        List<AskDTO> askDTOList = new ArrayList<>();
        askList.forEach(ask -> askDTOList.add(new AskDTO(ask)));
        return new AskListDTO(askDTOList);
    }

    @Override
    public AskDTO answerAsk(Integer askId, String answer) {
        Optional<Ask> ask = askRepository.findById(askId);
        if (ask.isPresent()) {
            Ask askEntity = ask.get();
            askEntity.setAskAnswer(answer);
            askEntity.setAskStatus(답변완료);
            askEntity.setAskCommentDateNow();  // 답변 등록 시각 설정
            askRepository.save(askEntity);
            logService.saveLog("Admin", LogStatus.등록,askEntity.getAskAnswer(),"Ask");
            return new AskDTO(askEntity);
        } else {
            throw new RuntimeException("Ask not found with id: " + askId);
        }
    }




    @Override
    public AskDTO createAsk(AskCreateDTO askDTO) {
        if (askDTO.getTitle() == null || askDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (askDTO.getContent() == null || askDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        Ask ask = new Ask();
        ask.setAskTitle(askDTO.getTitle());
        ask.setAskContent(askDTO.getContent());

        FranchiseOwner owner = franchiseOwnerRepository.findById(askDTO.getFranchiseOwnerCode())
                .orElseThrow(() -> new RuntimeException("Franchise owner not found"));
        ask.setFranchiseOwner(owner);

        // Admin 정보 가져오기
        Admin admin = adminRepository.findById(1)  // 예: 관리자 ID가 1인 경우
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        ask.setAdmin(admin);
        askRepository.save(ask);
        logService.saveLog("FranchiseOwner", LogStatus.등록,ask.getAskTitle(),"Ask");
        return new AskDTO(ask);
    }

    @Override
    public Ask updateAsk(int askCode, AskUpdateDTO askUpdateDTO) throws Exception {
        Ask ask = askRepository.findById(askCode)
                .orElseThrow(() -> new RuntimeException("Ask not found with id: " + askCode));

        if (ask.getAskStatus() != ASK_STATUS.답변대기) {
            throw new Exception("답변완료의 경우 수정할 수 없습니다.");
        }

        StringBuilder changes = new StringBuilder();

        if (!ask.getAskTitle().equals(askUpdateDTO.getTitle())) {
            changes.append(String.format("제목: '%s'에서 '%s'로 변경됨; ", ask.getAskTitle(), askUpdateDTO.getTitle()));
            ask.setAskTitle(askUpdateDTO.getTitle());
        }

        if (!ask.getAskContent().equals(askUpdateDTO.getContent())) {
            changes.append(String.format("내용: '%s'에서 '%s'로 변경됨; ", ask.getAskContent(), askUpdateDTO.getContent()));
            ask.setAskContent(askUpdateDTO.getContent());
        }
        ask.updateAskUpdateDate();  // Ensure this method is implemented to only update ask_update_date
        askRepository.save(ask);
        if (changes.length() > 0) {
            logService.saveLog("FranchiseOwner", LogStatus.수정, changes.toString(), "Ask");
        }
        return askRepository.save(ask);

    }

    @Override
    public FranchiseOwnerDTO getFranchiseOwnerDetails(int franchiseOwnerCode) {
        FranchiseOwner franchiseOwner = franchiseOwnerRepository.findByFranchiseOwnerCode(franchiseOwnerCode);
        if (franchiseOwner == null) {
            throw new EntityNotFoundException("Franchise owner not found");
        }
        return new FranchiseOwnerDTO(franchiseOwner);
    }


}
