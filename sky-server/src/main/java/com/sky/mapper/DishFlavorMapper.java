package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    //批量插入
    void insert(List<DishFlavor> flavors);

    @Delete("delete from sky_take_out.dish_flavor where dish_id=#{id}")
    void deleteByDishId(Long id);
}
