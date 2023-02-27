import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class QuartzExample {
    private static final Logger logger = LoggerFactory.getLogger(QuartzExample.class);
    public static void main(String[] args) {
        // Create a new scheduler
        Scheduler scheduler = null;
        try {
            logger.info("Standard Scheduler Set");
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            // Start the scheduler
            scheduler.start();

            // Define the job and tie it to our MyJob class
            JobDetail job = JobBuilder.newJob(MyJob.class)
                    .withIdentity("job1", "group1")
                    .build();

            // Define a trigger that fires every 5 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5)  // Set 5 second Timer
                            .repeatForever())
                    .build();

            // Tell the scheduler to schedule the job using the trigger

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            logger.info("Something went wrong with Scheduler",e);
        }
    }

    public static class MyJob implements Job {
        public void execute(JobExecutionContext context) throws JobExecutionException {
            // Job logic goes here
            System.out.println("Hello Quartz is running at " + Instant.now());
        }
    }
}
