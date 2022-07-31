package com.lkw.springbootrj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lkw.springbootrj.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}