package com.ohyoung.service.impl;

import com.ohyoung.service.ILogRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.LogRecord;

/**
 * @author ouyb01
 * @date 2022/1/24 21:49
 */
public class DefaultLogRecordServiceImpl implements ILogRecordService {

    private static final Logger log = LoggerFactory.getLogger(DefaultLogRecordServiceImpl.class);

    @Override
    public void record(LogRecord logRecord) {
        log.info("【logRecord】log={}", logRecord);
    }
}
