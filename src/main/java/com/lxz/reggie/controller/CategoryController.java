package com.lxz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxz.reggie.common.R;
import com.lxz.reggie.entity.Category;
import com.lxz.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("category: {}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        // 这里添加了泛型，但是employee的分页查询没有加。具体原因未知
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    // 这里参数不需要加requestbody的原因是，传递过来的数据名就是id，可以直接匹配，
    //  此时不需要解析json格式后再到实体类中进行匹配，因此不需要添加requestbody，
    //  但是此时要注意，前端传过来的数据名和这里的参数名要一样，否则无法匹配
    @DeleteMapping
    public R<String> delete(Long id) {
        log.info("删除分类，ID为：{}", id);
//        categoryService.removeById(id);
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    // 根据id修改分类信息
    @PutMapping
    // 只要前端传过来的数据格式是json就需要RequestBody
    // 如果是单独的参数，就不需要json
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    // 根据条件查询分类数据
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        // category.getType() != 0表示在对后面进行判断之前，先判断type中是否有值
        queryWrapper.eq(category.getType() != 0, Category::getType, category.getType());
        // 添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getCreateTime);

        //开始查询，将查询的数据放入一个list
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
