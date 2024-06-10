package com.akatsuki.pioms.log.service;

import com.akatsuki.pioms.log.aggregate.Log;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
class LogServiceTests {

    @MockBean
    private LogRepository logRepository;

    @Autowired
    private LogServiceImpl logService;

    private Log log;

    @BeforeEach
    void setUp() {
        log = new Log();
        log.setLogChanger("testChanger");
        log.setLogDate(LocalDateTime.now());
        log.setLogStatus(LogStatus.등록);
        log.setLogContent("Test content");
        log.setLogTarget("Test target");
    }

    @DisplayName("로그 저장 테스트 (Log 객체 사용)")
    @Test
    void saveLogWithLogObjectTest() {
        // Given
        when(logRepository.save(log)).thenReturn(log);

        // When
        logService.saveLog(log);

        // Then
        verify(logRepository, times(1)).save(log);
    }

    @DisplayName("로그 저장 테스트 (개별 매개변수 사용)")
    @Test
    void saveLogWithParametersTest() {
        // Given
        Log newLog = new Log("testChanger", LogStatus.등록, "Test content", "Test target");
        when(logRepository.save(any(Log.class))).thenReturn(newLog);

        // When
        logService.saveLog("testChanger", LogStatus.등록, "Test content", "Test target");

        // Then
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @DisplayName("전체 로그 조회 테스트")
    @Test
    void getAllLogsTest() {
        // Given
        when(logRepository.findAll()).thenReturn(List.of(log));

        // When
        List<Log> result = logService.getAllLogs();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(log.getLogChanger(), result.get(0).getLogChanger());
    }
}
