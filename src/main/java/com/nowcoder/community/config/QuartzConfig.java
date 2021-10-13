package com.nowcoder.community.config;

import com.nowcoder.community.quartz.PostRefreshScoreJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈进松
 * @Date: 2021/10/13/5:36
 * @Description:
 */
@Configuration
public class QuartzConfig {
    //配置JobDetail和Trigger

    /**
     * FactoryBean封装了Bean的实例化过程，方便Bean的创建
     * 将FactoryBean放入Spring容器中，取出来将会获取到他管理的Bean对象
     * 要想获取FactoryBean对象本身，要在BeanName前面加一个#号
     *
     */
    @Bean
    public JobDetailFactoryBean postRefreshJobDetail(){
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        //设置JobDetail的名字
        jobDetailFactoryBean.setName("postRefreshJobDetail");
        //设置分组
        jobDetailFactoryBean.setGroup("postRefreshJobDetailGroup");
        //设置关联的任务
        jobDetailFactoryBean.setJobClass(PostRefreshScoreJob.class);
        //持久化设置
        jobDetailFactoryBean.setDurability(true);
        //设置任务恢复标志
        jobDetailFactoryBean.setRequestsRecovery(true);
        return jobDetailFactoryBean;
    }
    //设置Trigger
    @Bean
    public SimpleTriggerFactoryBean postRefreshSimpleTrigger(JobDetail postRefreshJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        //设置名字
        factoryBean.setName("postRefreshSimpleTrigger");
        //设置分组
        factoryBean.setGroup("postRefreshSimpleTriggerGroup");
        //设置JobDetail，触发器触发时，执行那个任务
        factoryBean.setJobDetail(postRefreshJobDetail);
        //设置执行周期
        factoryBean.setRepeatInterval(1000*60*5);
        //用于存储数据和参数
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }

}