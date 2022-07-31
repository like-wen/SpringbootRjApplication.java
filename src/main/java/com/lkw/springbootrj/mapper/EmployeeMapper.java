package com.lkw.springbootrj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lkw.springbootrj.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{
}
