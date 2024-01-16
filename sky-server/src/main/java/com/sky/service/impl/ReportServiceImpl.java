package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
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

    /*
    * 统计营业额数据
    * */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //计算dateList
        List<LocalDate> dateList=new ArrayList<>();
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
}
