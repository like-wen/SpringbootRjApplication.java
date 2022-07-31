package com.lkw.springbootrj.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lkw.springbootrj.common.R;
import com.lkw.springbootrj.entity.Category;
import com.lkw.springbootrj.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("categorg:{}",category);
        categoryService.save(category);
        return R.success("保存成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page<Category> pageinfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pageinfo,queryWrapper);
        return R.success(pageinfo);

    }

    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类,id为:{}",id);
        categoryService.removeById(id);
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改信息:{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }


    //根据条件查询   分类数据
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加选择条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getType).orderByDesc(Category::getUpdateTime);
        List<Category> list=categoryService.list(queryWrapper);



        return R.success(list);
    }


}
