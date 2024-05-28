package com.akatsuki.pioms.log.service;

import com.akatsuki.pioms.log.aggregate.Log;
import com.akatsuki.pioms.log.etc.LogStatus;

import java.util.List;

public interface LogService {
    void saveLog(Log log);

    void saveLog(String logChanger, LogStatus logStatus, String content, String target);

    List<Log> getAllLogs();
}
