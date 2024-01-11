package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getByDishids(List<Long> ids);


    void saveBatch(List<SetmealDish> setmealDishes);

    void deleteByids(List<Long> ids);

    /*根据套餐id查询菜品*/
    List<SetmealDish> getBySetemealId(Long id);

    @Delete("delete from sky_take_out.setmeal_dish where setmeal_id=#{setmealId}")
    void deleteBySetmealId(Long setmealId);

}
