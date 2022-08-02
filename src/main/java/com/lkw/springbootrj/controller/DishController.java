package com.lkw.springbootrj.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value( "${reggie.path}")
    private String basePath;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);
        Set<String> keys = stringRedisTemplate.keys("dish_list*");//扫描前面带有dish的缓存存入集合,
        stringRedisTemplate.delete(keys);//再删除对应集合的缓存
        log.info("由于保存了dish_list,所以已经删除dish的所有缓存");

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
        Set<String> keys = stringRedisTemplate.keys("dish_list*");//扫描前面带有dish的缓存存入集合,
        stringRedisTemplate.delete(keys);//再删除对应集合的缓存
        log.info("由于更新了dish_list,所以已经删除dish的所有缓存");
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



    //一般存数据写在service层
    @GetMapping("/list")
    public  R<List<DishDto>> list(Dish dish) throws JsonProcessingException {
        List<DishDto> dishDtolist;

        //序列化
        String key="dish_list"+objectMapper.writeValueAsString(dish);

        //先查redis


        String value = stringRedisTemplate.opsForValue().get(key);

        //查到了就不用查mysql了
        if(value!=null) {
            log.info("在redis查到了");
            dishDtolist = objectMapper.readValue(value, List.class);
            return R.success(dishDtolist);
        }
       log.info("在redis没查到,继续到MySQL查");
        //没查到,就查mysql,然后
         LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
         queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
         queryWrapper.eq(Dish::getStatus,1);
         queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
         List<Dish> list=dishService.list(queryWrapper);
         dishDtolist = list.stream().map((item) -> {
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
         log.info("在mysql查到了,缓存到redis");
         //不存在,就进行缓存,并设置60分钟的过期时间
        stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(dishDtolist),60, TimeUnit.MINUTES);

        return R.success(dishDtolist);
    }
}






















