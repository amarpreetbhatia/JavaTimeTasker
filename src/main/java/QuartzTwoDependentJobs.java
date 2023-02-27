import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzTwoDependentJobs {
    private static final Logger logger = LoggerFactory.getLogger(QuartzTwoDependentJobs.class);
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("step1", "FirstJob");
            JobDetail jobStep1 = newJob(JobStep1.class)
                    .withIdentity("jobStep1", "group1")
                    .build();
            JobDetail jobStep2 = newJob(JobStep2.class)
                    .withIdentity("jobStep2", "group1")
                    .build();
            Trigger trigger1 = newTrigger()
                    .withIdentity("Trigger1", "group1")
                    .withPriority(1)
//                    .usingJobData("step1","")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(5)
                            .repeatForever())
                    .build();
            Trigger trigger2 = newTrigger()
                    .withIdentity("Trigger2", "group1")
                    .withPriority(2)
                    .usingJobData(jobDataMap)
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(10)
                            .repeatForever())
                    .build();
            scheduler.scheduleJob(jobStep1, trigger1);
            scheduler.scheduleJob(jobStep2, trigger2);
            scheduler.start();

        }catch (SchedulerException e) {
            logger.info("Something went wrong with Scheduler",e);
        }

    }

    public static class JobStep1 implements Job {
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Job Step 1 executed at " + new Date());
            context.getJobDetail().getJobDataMap().put("step1", "FirstJob");
        }
    }

    public static class JobStep2 implements Job {
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String previousJob = dataMap.getString("step1");
            if (previousJob != null && previousJob.equals("FirstJob")) {
                System.out.println("Step 2 executed at " + new Date());
            }else {
                System.out.println("Step 2 skipped because Step1 Job did not complete successfully.");
            }
        }
    }
}
