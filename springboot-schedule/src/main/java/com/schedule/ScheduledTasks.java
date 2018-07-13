package com.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Auther: zhao quan
 * @Date: 2018/7/12 10:30
 * @PACKAGE_NAME: com.schedule
 * @Description:  构建定时任务
 */
@Component
public class ScheduledTasks {

    private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private int fixedDelayCount = 1;

    private int fixedRateCount = 1;
    private int initialDelayCount = 1;
    private int cronCount = 1;


}
