package com.lkw.springbootrj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lkw.springbootrj.entity.User;
import com.lkw.springbootrj.mapper.UserMapper;
import com.lkw.springbootrj.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
