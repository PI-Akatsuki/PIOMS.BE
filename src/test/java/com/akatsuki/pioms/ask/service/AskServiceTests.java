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
import com.akatsuki.pioms.config.MockRedisConfig;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.log.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(MockRedisConfig.class)
@Transactional
class AskServiceTests {

    @MockBean
    private AskRepository askRepository;

    @MockBean
    private FranchiseOwnerRepository franchiseOwnerRepository;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private LogService logService;

    @Autowired
    private AskServiceImpl askService;

    private Ask ask1;
    private FranchiseOwner franchiseOwner;
    private Admin admin;

    @BeforeEach
    void setUp() {
        franchiseOwner = new FranchiseOwner();
        franchiseOwner.setFranchiseOwnerCode(1);
        franchiseOwner.setFranchiseOwnerId("가맹점주");
        franchiseOwner.setFranchiseOwnerPwd("password");
        franchiseOwner.setFranchiseOwnerName("김가맹");
        franchiseOwner.setFranchiseOwnerEmail("test@Test.com");
        franchiseOwner.setFranchiseOwnerPhone("0105656565");
        franchiseOwner.setFranchiseOwnerEnrollDate("20210506");

        admin = new Admin();
        admin.setAdminCode(1);
        admin.setAccessNumber(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8));
        admin.setAdminName("admin");
        admin.setAdminPwd("password");
        admin.setAdminEmail("test@test.com");
        admin.setAdminId("root");
        admin.setAdminPhone("01089898989");
        admin.setAdminRole("ROLE_ROOT");

        ask1 = new Ask(
                1, // askCode
                "문의 내용입니당", // askContent
                ASK_STATUS.답변대기, // askStatus
                "No answer yet", // askAnswer
                LocalDateTime.now(), // askEnrollDate
                LocalDateTime.now(), // askUpdateDate
                LocalDateTime.now(), // askCommentDate
                "문의 제목입니당", // askTitle
                franchiseOwner, // franchiseOwner
                admin // admin
        );

    }

    @DisplayName("전체 문의사항 조회")
    @Test
    void testGetAllAskList() {
        // Given
        List<Ask> askList = List.of(ask1);
        when(askRepository.findAllOrderByEnrollDateDesc()).thenReturn(askList);

        // When
        AskListDTO actualAskListDTO = askService.getAllAskList();

        // Then
        assertNotNull(actualAskListDTO);    //null인지
        assertEquals(1, actualAskListDTO.getAsks().size());     //list크기가 1인지
        assertEquals("문의 내용입니당", actualAskListDTO.getAsks().get(0).getAskContent());      //첫번째 문의사항이 Whaiis Pioms인지
    }

    @Test
    @DisplayName("상세 조회 성공 테스트")
    void testGetAskDetailsSuccess() {
        // Given
        int askCode = ask1.getAskCode();
        when(askRepository.findById(askCode)).thenReturn(Optional.of(ask1));

        // When
        AskDTO result = askService.getAskDetails(askCode);

        // Then
        assertNotNull(result);
        assertEquals(askCode, result.getAskCode());
        assertEquals("문의 내용입니당", result.getAskContent());
    }

    @DisplayName("답변 대기 중인 문의사항 조회")
    @Test
    void getWaitingForReplyAsks() {
        // Given
        Ask ask2 = new Ask(
                2, // askCode
                "다른 문의 내용입니다.", // askContent
                ASK_STATUS.답변완료, // askStatus
                "답변했습니다.", // askAnswer
                LocalDateTime.now(), // askEnrollDate
                LocalDateTime.now(), // askUpdateDate
                LocalDateTime.now(), // askCommentDate
                "다른 문의 제목입니다.", // askTitle
                franchiseOwner, // franchiseOwner
                admin // admin
        );

        List<Ask> askList = List.of(ask1, ask2);
        when(askRepository.findAllByStatusWaitingForReply()).thenReturn(List.of(ask1));

        // When
        AskListDTO actualAskListDTO = askService.getWaitingForReplyAsks();

        // Then
        assertNotNull(actualAskListDTO);
        assertEquals(1, actualAskListDTO.getAsks().size());
        assertEquals("문의 제목입니당", actualAskListDTO.getAsks().get(0).getAskTitle());
    }

    @DisplayName("점주별 작성한 문의사항 조회")
    @Test
    void getAsksByFranchiseOwnerId() {
        // Given
        FranchiseOwner otherOwner = new FranchiseOwner();
        otherOwner.setFranchiseOwnerCode(2);
        otherOwner.setFranchiseOwnerId("다른가맹점주");
        otherOwner.setFranchiseOwnerPwd("password2");
        otherOwner.setFranchiseOwnerName("이가맹");
        otherOwner.setFranchiseOwnerEmail("other@Test.com");
        otherOwner.setFranchiseOwnerPhone("0105656566");
        otherOwner.setFranchiseOwnerEnrollDate("20210507");

        Ask ask2 = new Ask(
                2, // askCode
                "다른 문의 내용입니다.", // askContent
                ASK_STATUS.답변대기, // askStatus
                "No answer yet", // askAnswer
                LocalDateTime.now(), // askEnrollDate
                LocalDateTime.now(), // askUpdateDate
                LocalDateTime.now(), // askCommentDate
                "다른 문의 제목입니다.", // askTitle
                otherOwner, // franchiseOwner
                admin // admin
        );

        List<Ask> askList = List.of(ask1, ask2);
        when(askRepository.findByFranchiseOwner_FranchiseOwnerCodeOrderByAskEnrollDateDesc(1)).thenReturn(List.of(ask1));

        // When
        AskListDTO actualAskListDTO = askService.getAsksByFranchiseOwnerId(1);

        // Then
        assertNotNull(actualAskListDTO);
        assertEquals(1, actualAskListDTO.getAsks().size());
        assertEquals("문의 내용입니당", actualAskListDTO.getAsks().get(0).getAskContent());
    }

    @DisplayName("문의사항 답변")
    @Test
    void answerAsk() {
        // Given
        int askId = ask1.getAskCode();
        String answer = "답변했습니다.";
        when(askRepository.findById(askId)).thenReturn(Optional.of(ask1));

        // When
        AskDTO result = askService.answerAsk(askId, answer);

        // Then
        assertNotNull(result);
        assertEquals(answer, result.getAskAnswer());
        assertEquals(ASK_STATUS.답변완료, result.getAskStatus());
    }

    @DisplayName("문의사항 작성")
    @Test
    void createAsk() {
        // Given
        AskCreateDTO askCreateDTO = new AskCreateDTO();
        askCreateDTO.setTitle("새 문의사항 제목");
        askCreateDTO.setContent("새 문의사항 내용");
        askCreateDTO.setFranchiseOwnerCode(1);

        Ask newAsk = new Ask();
        newAsk.setAskTitle(askCreateDTO.getTitle());
        newAsk.setAskContent(askCreateDTO.getContent());
        newAsk.setFranchiseOwner(franchiseOwner);
        newAsk.setAdmin(admin);

        when(franchiseOwnerRepository.findById(askCreateDTO.getFranchiseOwnerCode())).thenReturn(Optional.of(franchiseOwner));
        when(adminRepository.findById(1)).thenReturn(Optional.of(admin));
        when(askRepository.save(any(Ask.class))).thenReturn(newAsk);

        // When
        AskDTO result = askService.createAsk(askCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("새 문의사항 제목", result.getAskTitle());
        assertEquals("새 문의사항 내용", result.getAskContent());
    }

    @DisplayName("문의사항 수정")
    @Test
    void updateAsk() throws Exception {
        // Given
        int askCode = ask1.getAskCode();
        AskUpdateDTO askUpdateDTO = new AskUpdateDTO();
        askUpdateDTO.setTitle("수정된 제목!");
        askUpdateDTO.setContent("수정된 내용");

        when(askRepository.findById(askCode)).thenReturn(Optional.of(ask1));
        when(askRepository.save(any(Ask.class))).thenReturn(ask1);

        // When
        Ask result = askService.updateAsk(askCode, askUpdateDTO);

        // Then
        assertNotNull(result);
        assertEquals("수정된 제목!", result.getAskTitle());
        assertEquals("수정된 내용", result.getAskContent());
    }
}
