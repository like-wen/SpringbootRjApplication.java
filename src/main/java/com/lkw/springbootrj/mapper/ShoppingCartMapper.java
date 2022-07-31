package com.lkw.springbootrj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lkw.springbootrj.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}
