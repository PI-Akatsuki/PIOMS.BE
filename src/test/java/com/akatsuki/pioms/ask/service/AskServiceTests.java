//package com.akatsuki.pioms.ask.service;
//
//import com.akatsuki.pioms.ask.aggregate.ASK_STATUS;
//import com.akatsuki.pioms.ask.aggregate.Ask;
//import com.akatsuki.pioms.ask.dto.AskCreateDTO;
//import com.akatsuki.pioms.ask.dto.AskDTO;
//import com.akatsuki.pioms.ask.dto.AskListDTO;
//import com.akatsuki.pioms.ask.dto.AskUpdateDTO;
//import com.akatsuki.pioms.ask.repository.AskRepository;
//import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
//import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
//import com.akatsuki.pioms.log.service.LogService;
//import com.akatsuki.pioms.admin.aggregate.Admin;
//import com.akatsuki.pioms.admin.repository.AdminRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class AskServiceTests {
//
//    private final AskServiceImpl askService;
//    private final AskRepository askRepository;
//    private final FranchiseOwnerRepository franchiseOwnerRepository;
//    private final AdminRepository adminRepository;
//    private final LogService logService;
//
//    @Autowired
//    public AskServiceTests(AskServiceImpl askService,
//                           AskRepository askRepository,
//                           FranchiseOwnerRepository franchiseOwnerRepository,
//                           AdminRepository adminRepository,
//                           LogService logService) {
//        this.askService = askService;
//        this.askRepository = askRepository;
//        this.franchiseOwnerRepository = franchiseOwnerRepository;
//        this.adminRepository = adminRepository;
//        this.logService = logService;
//    }
//    private Ask ask1;
//
//    @BeforeEach
//    void setUp() {
//        FranchiseOwner franchiseOwner = new FranchiseOwner();
//        franchiseOwner.setFranchiseOwnerCode(1);
//        franchiseOwner.setFranchiseOwnerId("가맹점주");
//        franchiseOwner.setFranchiseOwnerPwd("password");
//        franchiseOwner.setFranchiseOwnerName("김가맹");
//        franchiseOwner.setFranchiseOwnerEmail("test@Test.com");
//        franchiseOwner.setFranchiseOwnerPhone("0105656565");
//        franchiseOwner.setFranchiseOwnerEnrollDate("20210506");
//
//        franchiseOwnerRepository.save(franchiseOwner);
//
//        Admin admin = new Admin();
//        admin.setAdminCode(1);
//        admin.setAccessNumber(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8));
//        admin.setAdminName("admin");
//        admin.setAdminPwd("password");
//        admin.setAdminEmail("test@test.com");
//        admin.setAdminId("root");
//        admin.setAdminPhone("01089898989");
//        admin.setAdminRole("ROLE_ROOT");
//        adminRepository.save(admin);
//
//        ask1 = new Ask(
//                1, // askCode
//                "What is PIOMS?", // askContent
//                ASK_STATUS.답변대기, // askStatus
//                "No answer yet", // askAnswer
//                LocalDateTime.now(), // askEnrollDate
//                LocalDateTime.now(), // askUpdateDate
//                LocalDateTime.now(), // askCommentDate
//                "Important Question", // askTitle
//                franchiseOwner, // franchiseOwner
//                admin // admin
//        );
//        askRepository.save(ask1);
//    }
//
//    @DisplayName("전체 문의사항 조회")
//    @Test
//    void testGetAllAskList() {
//        // Given
//        List<Ask> askList = askRepository.findAll();
//        List<AskDTO> expectedAskDTOList = new ArrayList<>();
//        askList.forEach(ask -> expectedAskDTOList.add(new AskDTO(ask)));
//        AskListDTO expectedAskListDTO = new AskListDTO(expectedAskDTOList);
//
//        // When
//        AskListDTO actualAskListDTO = askService.getAllAskList();
//
//        // Then
//        assertNotNull(actualAskListDTO);
//        assertEquals(expectedAskListDTO.getAsks().size(), actualAskListDTO.getAsks().size());
//        for (int i = 0; i < expectedAskListDTO.getAsks().size(); i++) {
//            assertEquals(expectedAskListDTO.getAsks().get(i).getAskContent(), actualAskListDTO.getAsks().get(i).getAskContent());
//        }
//    }
//
//    @Test
//    @DisplayName("상세 조회 성공 테스트")
//    void testGetAskDetailsSuccess() {
//        // Given
//        int askCode = ask1.getAskCode();
//
//        // When
//        AskDTO result = askService.getAskDetails(askCode);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(askCode, result.getAskCode());
//        assertEquals("What is PIOMS?", result.getAskContent());
//    }
//
//    @DisplayName("답변 대기 중인 문의사항 조회")
//    @Test
//    void getWaitingForReplyAsks() {
//        //Given
//        ASK_STATUS askStatus= ASK_STATUS.답변대기;
//        List<Ask> askList = askRepository.findAllByStatusWaitingForReply();
//        List<AskDTO> expectedAskDTOList = new ArrayList<>();
//        askList.forEach(ask -> expectedAskDTOList.add(new AskDTO(ask)));
//        AskListDTO expectedAskListDTO = new AskListDTO(expectedAskDTOList);
//
//        //when
//        AskListDTO actualAskListDTO = askService.getWaitingForReplyAsks();
//
//        //Then
//        assertNotNull(actualAskListDTO);
//        assertEquals(expectedAskListDTO.getAsks().size(),actualAskListDTO.getAsks().size());
//        for (int i = 0; i < expectedAskListDTO.getAsks().size(); i++) {
//            assertEquals(expectedAskListDTO.getAsks().get(i).getAskTitle(),actualAskListDTO.getAsks().get(i).getAskTitle());
//        }
//    }
//
//    @DisplayName("점주별 작성한 문의사항 조회")
//    @Test
//    void getAsksByFranchiseOwnerId() {
//        // Given
//        int franchiseOwnerId = 1;
//        List<Ask> askList = askRepository.findByFranchiseOwner_FranchiseOwnerCode(franchiseOwnerId);
//        List<AskDTO> expectedAskDTOList = new ArrayList<>();
//        askList.forEach(ask -> expectedAskDTOList.add(new AskDTO(ask)));
//        AskListDTO expectedAskListDTO = new AskListDTO(expectedAskDTOList);
//
//        // When
//        AskListDTO actualAskListDTO = askService.getAsksByFranchiseOwnerId(franchiseOwnerId);
//
//        // Then
//        assertNotNull(actualAskListDTO);
//        assertEquals(expectedAskListDTO.getAsks().size(), actualAskListDTO.getAsks().size());
//        for (int i = 0; i < expectedAskListDTO.getAsks().size(); i++) {
//            assertEquals(expectedAskListDTO.getAsks().get(i).getAskContent(), actualAskListDTO.getAsks().get(i).getAskContent());
//        }
//    }
//
//    @DisplayName("문의사항 답변")
//    @Test
//    void answerAsk() {
//        // Given
//        int askId = ask1.getAskCode();
//        String answer = "This is the answer.";
//
//        // When
//        AskDTO result = askService.answerAsk(askId, answer);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(answer, result.getAskAnswer());
//        assertEquals(ASK_STATUS.답변완료, result.getAskStatus());
//    }
//
//    @DisplayName("문의사항 작성")
//    @Test
//    void createAsk() {
//        // Given
//        AskCreateDTO askCreateDTO = new AskCreateDTO();
//        askCreateDTO.setTitle("New Question");
//        askCreateDTO.setContent("What is the new feature?");
//        askCreateDTO.setFranchiseOwnerCode(1);
//
//        // When
//        AskDTO result = askService.createAsk(askCreateDTO);
//
//        // Then
//        assertNotNull(result);
//        assertEquals("New Question", result.getAskTitle());
//        assertEquals("What is the new feature?", result.getAskContent());
//    }
//
//    @DisplayName("문의사항 수정")
//    @Test
//    void updateAsk() throws Exception {
//        // Given
//        int askCode = ask1.getAskCode();
//        AskUpdateDTO askUpdateDTO = new AskUpdateDTO();
//        askUpdateDTO.setTitle("Updated Question");
//        askUpdateDTO.setContent("What is the updated feature?");
//
//        // When
//        Ask result = askService.updateAsk(askCode, askUpdateDTO);
//
//        // Then
//        assertNotNull(result);
//        assertEquals("Updated Question", result.getAskTitle());
//        assertEquals("What is the updated feature?", result.getAskContent());
//    }
//}