package com.lkw.springbootrj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lkw.springbootrj.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}