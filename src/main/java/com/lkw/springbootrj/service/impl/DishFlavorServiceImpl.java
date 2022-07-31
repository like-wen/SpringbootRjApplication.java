package com.lkw.springbootrj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lkw.springbootrj.entity.DishFlavor;
import com.lkw.springbootrj.mapper.DishFlavorMapper;
import com.lkw.springbootrj.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
