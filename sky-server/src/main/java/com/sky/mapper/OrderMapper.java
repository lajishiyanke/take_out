package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from sky_take_out.orders where id=#{id}")
    Orders getByid(Long id);


    void update(Orders orders);

    @Select("select count(id) from sky_take_out.orders where status = #{status}")
    Integer countStatus(Integer toBeConfirmed);

    @Select("select * from sky_take_out.orders where status= #{pendingPayment} and order_time < #{minutes}")
    List<Orders> getByStatusAndOrderTime(Integer pendingPayment, LocalDateTime minutes);

    /**
     * 根据订单号查询订单
     */
    @Select("select * from sky_take_out.orders where number = #{orderNumber}")
    Orders getByNumber(String outTradeNo);

    Double sumByMap(Map map);
}
