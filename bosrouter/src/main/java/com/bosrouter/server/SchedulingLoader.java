package com.bosrouter.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static org.quartz.JobBuilder.*;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.bosrouter.mbta.gtfs.realtime.FeedReader;

public class SchedulingLoader implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            
            //TODO job building should not be here.
            JobDetail job = newJob(FeedReader.class)
            	    		.withIdentity("job1", "group1")
            	    		.build();

            	// Trigger the job to run now, and then repeat every 40 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
            	                            .withIdentity("trigger1", "group1")
            	                            .startNow()
            	                            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            	            		        .withIntervalInSeconds(40)
            	            		        .repeatForever())
            	                            .build();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(job, trigger);
            
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	

}
