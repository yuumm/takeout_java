package com.lxz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxz.reggie.common.R;
import com.lxz.reggie.dto.DishDto;
import com.lxz.reggie.entity.Dish;
import com.lxz.reggie.service.DishFlavorService;
import com.lxz.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    // 菜品信息分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        // 构造DishDto的构造器的原因是，Dish里面只有categoryid，这个无法和前端菜品分类字段也就是（categoryname）相对应，
        // 导致了该列没有数据。DishDto继承自Dish，同时添加了categoryname，因此创建该对象，然后将dish中的数据放进该字段中，
        // 再传递给前端
        Page<DishDto> dishDtoPage = new Page<>();

        // 条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        // 执行分页查询
        dishService.page(pageInfo, queryWrapper);

        // 对象拷贝
        // 第三个参数表示该数据不进行拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "recods");

        return R.success(pageInfo);
    }

    // 根据id查询菜品信息和口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getWithFlavor(id);
        return R.success(dishDto);
    }

    // 修改菜品
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    // 根据条件查询菜品数据
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        // 构造条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                .eq(Dish::getStatus, 1);

        // 添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }
}
