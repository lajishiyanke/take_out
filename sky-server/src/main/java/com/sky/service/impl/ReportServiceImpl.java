package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;
    /*
    * 统计营业额数据
    * */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //计算dateList
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        //计算每日营业额
        List<Double> turnoverList=new ArrayList<>();
        for (LocalDate date : dateList) {
            //查询date对应的营业额 状态为：已完成订单 的金额合计
            //由于LocalDate只有日期没有时分秒，用MIN MAX可得到那一天的00；00；00 23；59；59
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date,LocalTime.MAX);
            //构造一个Map 把查询条件扔进去
            Map map =new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover= orderMapper.sumByMap(map);
            turnover=turnover==null?0.0:turnover;
            turnoverList.add(turnover);
        }
        //StringUtils.join()方法
        return TurnoverReportVO.builder()
                    .dateList(StringUtils.join(dateList,","))
                    .turnoverList(StringUtils.join(turnoverList,","))
                    .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        //计算dateList
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        //靠卡住crete_time即可
        //每天新增 0<crete_time<23:59
        List<Integer> newUserList=new ArrayList<>();
        //每天总计 create_time<23:59
        List<Integer> totalUserList=new ArrayList<>();
        for (LocalDate date:dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date,LocalTime.MAX);
            //先查总数
            Map map =new HashMap<>();
            map.put("end",endTime);
            Integer total_user=userMapper.conutByMap(map);
            totalUserList.add(total_user);
            //新增
            map.put("begin",beginTime);
            Integer new_user=userMapper.conutByMap(map);
            newUserList.add(new_user);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();
    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        //计算dateList
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        //记录每日订单总数
        List<Integer> totalOrderList=new ArrayList<>();
        //记录每日有效订单数 状态已完成
        List<Integer> completedOrderList=new ArrayList<>();

        for (LocalDate date:dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date,LocalTime.MAX);
            //查询每天的订单总数
            Map map =new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            Integer total_order=orderMapper.conutByMap(map);
            totalOrderList.add(total_order);
            //查询每天有效订单数
            map.put("status",Orders.COMPLETED);
            Integer Completed_order=orderMapper.conutByMap(map);
            completedOrderList.add(Completed_order);
        }
        //总数
        Integer total = totalOrderList.stream().reduce(Integer::sum).get();
        Integer total_Completed = completedOrderList.stream().reduce(Integer::sum).get();
        //转成double
        Double rate = 0.0;
        if(total!=0){
            rate = total_Completed.doubleValue()/total;
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(totalOrderList,","))
                .validOrderCountList(StringUtils.join(completedOrderList,","))
                .totalOrderCount(total)
                .validOrderCount(total_Completed)
                .orderCompletionRate(rate)
                .build();
    }
}
