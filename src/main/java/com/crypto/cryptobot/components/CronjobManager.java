package com.crypto.cryptobot.components;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CronjobManager {

    private boolean taskStarted = false;

    @Scheduled(cron = "${request.frequancy.cron}")
    public void scheduleTask() {
        if (taskStarted) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss.SSS");

            String strDate = dateFormat.format(new Date());
            System.out.println("Cron job Scheduler: Job running at - " + strDate);
        }
    }

    public void startJob() {
        taskStarted = true;
    }

    public void stopJob() {
        taskStarted = false;
    }
}
