package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    PageResult pageQuery(int page, int pageSize, Integer status);

    OrderVO details(Long id);

    void userCancelById(Long id) throws Exception;

    void repetition(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO)throws Exception;

    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    void delivery(Long id);

    void complete(Long id);
}
