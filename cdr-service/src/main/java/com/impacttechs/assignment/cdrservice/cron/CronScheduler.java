package com.impacttechs.assignment.cdrservice.cron;

import com.impacttechs.assignment.cdrservice.abstraction.ICDRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CronScheduler {

    private final ICDRService cdrService;

    @Autowired
    public CronScheduler(ICDRService cdrService) {
        this.cdrService = cdrService;
    }

    @Scheduled(cron = "${impacttechs.crm.cron.expression:0 0 20 * * ?}")
    public void cdrUpdatingScheduler() throws Exception {
        log.info("Cron job triggered for updating CDR From PBX exchange");
        String content = cdrService.updateAllCDRRecords();
        log.info("Cron finished executing. Result: [{}] ", content);
    }
}
