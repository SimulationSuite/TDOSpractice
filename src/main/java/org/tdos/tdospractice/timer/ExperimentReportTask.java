package org.tdos.tdospractice.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.tdos.tdospractice.service.ExperimentReportService;
import org.tdos.tdospractice.service.ExperimentService;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class ExperimentReportTask {

    @Autowired
    private ExperimentReportService experimentReportService;

    @Scheduled(cron = "0 0 0 * * ?")
    private void configureTasks() {
        experimentReportService.updateExperimentReportByEndtime();
    }
}
