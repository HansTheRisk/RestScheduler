package main.controller;

import main.resource.JobResource;
import main.resource.JobResource.Unit;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.quartz.JobBuilder.newJob;

@RestController
public class SchedulerController {

    private Scheduler scheduler;

    @Autowired
    public SchedulerController(Scheduler scheduler) throws SchedulerException {
        this.scheduler = scheduler;
        scheduler.start();
    }

    // Get Job
    @GetMapping("/jobu/{key}")
    public ResponseEntity<JobDetail> getJob(@PathVariable String key) throws SchedulerException {
        JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(key));
        if(jobDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(scheduler.getJobDetail(JobKey.jobKey(key)));
    }

//    // Get Jobs
//    public ResponseEntity<List<JobResource>> getJobs() {
//
//    }

    // Schedule a Job
    @PostMapping("/jobu")
    public ResponseEntity createJob(@RequestBody JobResource job) throws SchedulerException {
        // Create Job
        JobDetail jobDetail = newJob(PrintJob.class).withIdentity(job.getName())
                                                    .usingJobData("text", job.getContent())
                                                    .build();
        // Create Job Trigger
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(jobDetail.getKey().getName())
                                                                   .usingJobData("text", job.getContent());

        if(job.getStart() == null) {
            triggerBuilder.startNow();
        } else {
            triggerBuilder.startAt(job.getStart());
        }

        if(job.getEnd() == null) {
            scheduleBuilder.repeatForever();
        } else {
            triggerBuilder.endAt(job.getEnd());
        }

        switch (job.getIntervalUnit()) {
            case MILLIS -> scheduleBuilder.withIntervalInMilliseconds(job.getInterval());
            case SECONDS -> scheduleBuilder.withIntervalInSeconds(job.getInterval());
            case MINUTES -> scheduleBuilder.withIntervalInMinutes(job.getInterval());
            case HOURS -> scheduleBuilder.withIntervalInHours(job.getInterval());
        }
        System.out.println(scheduler.scheduleJob(jobDetail, triggerBuilder.withSchedule(scheduleBuilder).build()));


        return ResponseEntity.ok().build();
    }
    // Modify a Job
    // Delete a Job

}
