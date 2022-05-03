package com.example;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.example.job.MyDataFlowJob;
import com.example.job.MySimpleJob;

public class App {
    public static void main(String[] args) {
        System.out.println("hello");
        new JobScheduler(zkCenter(), configurationScript()).init();
    }

    /**
     * 注册中心
     * @return
     */
    public static CoordinatorRegistryCenter zkCenter() {
        ZookeeperConfiguration zc = new ZookeeperConfiguration("localhost:2181", "simple-job");
        CoordinatorRegistryCenter coordinatorRegistryCenter = new ZookeeperRegistryCenter(zc);
        coordinatorRegistryCenter.init();
        return coordinatorRegistryCenter;
    }
    /**
     * job 配置
     * @return
     */
    public static LiteJobConfiguration configuration() {
        //job核心配置
        JobCoreConfiguration jcc = JobCoreConfiguration
                .newBuilder("mySimpleJob", "0/5 * * * * ?", 2)
                .build();
        //job类型配置
        JobTypeConfiguration jtc = new SimpleJobConfiguration(jcc, MySimpleJob.class.getCanonicalName());
        //job根的配置
        LiteJobConfiguration ljc = LiteJobConfiguration.newBuilder(jtc).overwrite(true).build();
        return ljc;
    }

    public static LiteJobConfiguration configurationDataflow() {
        //job核心配置
        JobCoreConfiguration jcc = JobCoreConfiguration
                .newBuilder("myDataflowJob", "0/10 * * * * ?", 2)
                .build();
        //job类型配置
        JobTypeConfiguration jtc = new DataflowJobConfiguration(jcc, MyDataFlowJob.class.getCanonicalName(), true);
        //job根的配置
        LiteJobConfiguration ljc = LiteJobConfiguration.newBuilder(jtc).overwrite(true).build();
        return ljc;
    }

    public static LiteJobConfiguration configurationScript() {
        //job核心配置
        JobCoreConfiguration jcc = JobCoreConfiguration
                .newBuilder("myScriptJob", "0/10 * * * * ?", 2)
                .build();
        //job类型配置
        var jtc = new ScriptJobConfiguration(jcc, "/Users/ihao/Desktop/test.sh");
        //job根的配置
        LiteJobConfiguration ljc = LiteJobConfiguration.newBuilder(jtc).overwrite(true).build();
        return ljc;
    }
}
