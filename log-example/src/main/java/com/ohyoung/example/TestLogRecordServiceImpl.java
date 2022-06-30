package com.ohyoung.example;

import com.ohyoung.entity.LogRecordPO;
import com.ohyoung.service.ILogRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ouyb01
 * @date 2022/1/24 21:49
 */
@Component
public class TestLogRecordServiceImpl implements ILogRecordService {

    private static final Logger log = LoggerFactory.getLogger(TestLogRecordServiceImpl.class);

    @Override
    public void record(LogRecordPO logRecord, boolean isSuccess) {
        log.info("【logRecord custom implement】log={}", logRecord);
    }
}
