//package com.akatsuki.pioms.log.service;
//
//import com.akatsuki.pioms.log.aggregate.Log;
//import com.akatsuki.pioms.log.etc.LogStatus;
//import com.akatsuki.pioms.log.repository.LogRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@Transactional
//class LogServiceImplTest {
//    private final LogServiceImpl logService;
//    private final LogRepository logRepository;
//
//    @Autowired
//    public LogServiceImplTest(LogServiceImpl logService, LogRepository logRepository) {
//        this.logService = logService;
//        this.logRepository = logRepository;
//    }
//
//    @BeforeEach
//    void setUp() {
//        logRepository.deleteAll();
//    }
//
//    @DisplayName("로그 저장 테스트")
//    @Test
//    void testSaveLogWithLogObject() {
//        // Given
//        Log log = new Log("user1", LogStatus.등록, "This is a log message", "SomeTarget");
//
//        // When
//        logService.saveLog(log);
//
//        // Then
//        Log savedLog = logRepository.findAll().get(0);
//        assertNotNull(savedLog);
//        assertEquals("user1", savedLog.getLogChanger());
//        assertEquals(LogStatus.등록, savedLog.getLogStatus());
//        assertEquals("This is a log message", savedLog.getLogContent());
//        assertEquals("SomeTarget", savedLog.getLogTarget());
//    }
//
//    @DisplayName("로그 저장 테스트 (개별 매개변수)")
//    @Test
//    void testSaveLogWithParameters() {
//        // Given
//        String logChanger = "user1";
//        LogStatus logStatus = LogStatus.등록;
//        String content = "This is a log message";
//        String target = "SomeTarget";
//
//        // When
//        logService.saveLog(logChanger, logStatus, content, target);
//
//        // Then
//        Log savedLog = logRepository.findAll().get(0);
//        assertNotNull(savedLog);
//        assertEquals(logChanger, savedLog.getLogChanger());
//        assertEquals(logStatus, savedLog.getLogStatus());
//        assertEquals(content, savedLog.getLogContent());
//        assertEquals(target, savedLog.getLogTarget());
//    }
//}
