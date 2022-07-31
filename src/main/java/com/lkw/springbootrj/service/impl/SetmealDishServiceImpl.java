package com.lkw.springbootrj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lkw.springbootrj.entity.SetmealDish;
import com.lkw.springbootrj.mapper.SetmealDishMapper;
import com.lkw.springbootrj.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
