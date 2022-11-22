package com.lxz.reggie.controller;

// 套餐管理

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxz.reggie.common.R;
import com.lxz.reggie.dto.SetmealDto;
import com.lxz.reggie.entity.Category;
import com.lxz.reggie.entity.Setmeal;
import com.lxz.reggie.service.CategoryService;
import com.lxz.reggie.service.SetmealDishService;
import com.lxz.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.dsig.keyinfo.PGPData;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    // 新增套餐
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息: {}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    // 分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造分页查询构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        // 构造dtoPage的原因在DishController中写到。
        Page<SetmealDto> dtoPage = new Page<>();

        // 构造条件查询构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            queryWrapper.like(Setmeal::getName, name);
        }
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        // 对象拷贝
        // 第三个参数表示dtoPage中不需要拷贝的字段。主要原因是，records是一个Setmeal类型的列表
        // 但是我们拷贝后数据类型是SetmealDto，因此要进行数据转化后才能拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        // 通过stream流对records进行类型修改
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝，实现类型的转换
            BeanUtils.copyProperties(item, setmealDto);
            // 分类id
            Long categoryId = item.getCategoryId();
            log.info(String.valueOf(categoryId));
            // 根据分类id查询对应的数据
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    // 删除套餐
    @DeleteMapping
    public R<String> delete(@RequestParam(value = "ids") List<Long> ids) {
        log.info("ids: {}", ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }
}
