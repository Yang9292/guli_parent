package com.yang.servicestatis.schedule;

import com.yang.servicestatis.service.StatisticsDailyService;
import com.yang.servicestatis.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //在每天凌晨一点，将前一天的数据进行添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2(){
        statisticsDailyService.regesterCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }

}