package com.lkw.springbootrj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lkw.springbootrj.entity.OrderDetail;
import com.lkw.springbootrj.mapper.OrderDetailMapper;
import com.lkw.springbootrj.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}