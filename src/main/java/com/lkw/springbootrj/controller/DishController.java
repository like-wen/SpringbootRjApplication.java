package com.lkw.springbootrj.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.lkw.springbootrj.common.R;
import com.lkw.springbootrj.dto.DishDto;
import com.lkw.springbootrj.entity.Category;
import com.lkw.springbootrj.entity.Dish;
import com.lkw.springbootrj.entity.DishFlavor;
import com.lkw.springbootrj.service.CategoryService;
import com.lkw.springbootrj.service.DishFlavorService;
import com.lkw.springbootrj.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;


    @Value( "${reggie.path}")
    private String basePath;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }


    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status,Long ids){
        UpdateWrapper<Dish> Wrapper = new UpdateWrapper<>();
        Wrapper.eq("id",ids);
        Wrapper.set("status",status);
        dishService.update(null,Wrapper);
        return R.success("状态更新成功");
    }

    @DeleteMapping
    public R<String> delete(Long ids){

        Dish dish = dishService.getById(ids);
        String image = dish.getImage();
        log.info(image);
        File file = new File(basePath + image);
        file.delete();
        dishService.removeById(ids);
        return R.success("删除成功");
    }


    @GetMapping("/list")
    public  R<List<DishDto>> list(Dish dish){
         LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
         queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
         queryWrapper.eq(Dish::getStatus,1);
         queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
         List<Dish> list=dishService.list(queryWrapper);
        List<DishDto> dishDtolist = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id=?
            List<DishFlavor> dishFlavorList=dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList );
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtolist);
    }
}






















