package org.terracotta.quartz.upgradability.serialization.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Test {

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        //创建调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        //定义一个触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1") //定义名称和所属的组
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2) //每隔2秒执行一次
                        .withRepeatCount(10)) //总共执行11次(第一次执行不基数)
                .build();

        //定义一个JobDetail
        JobDetail job = JobBuilder.newJob(MailJob.class) //指定干活的类MailJob
                .withIdentity("mailjob1", "mailgroup") //定义任务名称和分组
                .usingJobData("email", "admin@10086.com") //定义属性
                .build();

        //调度加入这个job
        scheduler.scheduleJob(job, trigger);

        //启动
        scheduler.start();

        //等待20秒，让前面的任务都执行完了之后，再关闭调度器
        Thread.sleep(20000);
        scheduler.shutdown(true);
    }
}
