package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

//定时任务类
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    //处理订单超时的方法
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void processTimeoutOrder(){
        log.info("每分钟处理一次{}", LocalDateTime.now());
        //待付款状态且下单时间小于当前时间减去15分钟
        LocalDateTime minutes = LocalDateTime.now().plusMinutes(-15);
        //传两参数查询订单
        List<Orders> ordersList=orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT,minutes);
        //如果存在这样的订单
        if(ordersList !=null&&ordersList.size()>0){
            for(Orders orders:ordersList){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    //处理一直在派送中的订单
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("处理一直处于派送中的订单");
        //当前时间减1h
        LocalDateTime minutes = LocalDateTime.now().plusMinutes(-60);
        //传两参数查询订单
        List<Orders> ordersList=orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT,minutes);
        if(ordersList !=null&&ordersList.size()>0){
            for(Orders orders:ordersList){
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
