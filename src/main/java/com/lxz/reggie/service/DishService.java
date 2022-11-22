package com.lxz.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxz.reggie.dto.DishDto;
import com.lxz.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品数据和菜品口味数据，因此需要分别操作dish和dishflavor两个表
    public void saveWithFlavor(DishDto dishDto);
    // 根据id查询菜品信息和对应的口味信息
    public DishDto getWithFlavor(Long id);
    // 更新菜品信息，同时更新口味信息
    public void updateWithFlavor(DishDto dishDto);
}
