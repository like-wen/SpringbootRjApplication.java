package com.lkw.springbootrj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lkw.springbootrj.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
