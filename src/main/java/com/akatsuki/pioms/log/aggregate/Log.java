package com.akatsuki.pioms.log.aggregate;

import com.akatsuki.pioms.log.etc.LogStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "log")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_code")
    private int logCode;

    @Column(name = "log_changer", nullable = false, length = 255)
    private String logChanger;

    @Column(name = "log_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime logDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('등록', '수정', '삭제')", nullable = false)
    private LogStatus logStatus;

    @Column(name = "log_content", nullable = false, length = 255)
    private String logContent;

    @Column(name = "log_target", nullable = false, length = 255)
    private String logTarget;

    public Log(String logChanger, LogStatus logStatus, String logContent, String logTarget) {
        this.logChanger = logChanger;
        this.logDate = LocalDateTime.now();
        this.logStatus = logStatus;
        this.logContent = logContent;
        this.logTarget = logTarget;
    }
}