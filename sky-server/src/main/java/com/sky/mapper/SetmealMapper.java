package com.sky.mapper;

import com.github.pagehelper.Constant;
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.INSERT)
    void save(Setmeal setmeal);


    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /*批量删除*/
    void deleteByids(List<Long> ids);

    @Select("select * from sky_take_out.setmeal where id=#{id}")
    Setmeal getById(Long id);
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

}
