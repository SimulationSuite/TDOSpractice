package org.tdos.tdospractice.kvm;

import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

    @Bean("ImageQueue")
    public ActiveMQQueue imageQueue() {
        return new ActiveMQQueue("image-Queue");
    }

    /**
     * 消息重试配置项
     *
     * @return
     */
    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setUseExponentialBackOff(true);//是否在每次失败重发是，增长等待时间
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);//设置重发最大拖延时间，-1 表示没有拖延，只有setUseExponentialBackOff(true)时生效
        redeliveryPolicy.setMaximumRedeliveries(10);//重发次数
        redeliveryPolicy.setInitialRedeliveryDelay(1);//重发时间间隔
        redeliveryPolicy.setBackOffMultiplier(2);//第一次失败后重发前等待500毫秒，第二次500*2，依次递增
        redeliveryPolicy.setUseCollisionAvoidance(false);//是否避免消息碰撞
        return redeliveryPolicy;
    }

}
