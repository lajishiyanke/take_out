package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {


    void insert(User user);

    //根据openid查询
    @Select("select * from sky_take_out.user where openid=#{openid}")
    User getByOpenId(String openid);

    @Select("select * from sky_take_out.user where id=#{userId}")
    User getById(Long userId);
}
