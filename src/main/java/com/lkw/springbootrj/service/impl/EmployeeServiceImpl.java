package com.lkw.springbootrj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lkw.springbootrj.entity.Employee;
import com.lkw.springbootrj.mapper.EmployeeMapper;
import com.lkw.springbootrj.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
