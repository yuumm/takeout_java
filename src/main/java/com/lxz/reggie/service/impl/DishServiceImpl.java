package com.lxz.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxz.reggie.dto.DishDto;
import com.lxz.reggie.entity.Dish;
import com.lxz.reggie.entity.DishFlavor;
import com.lxz.reggie.mapper.DishMapper;
import com.lxz.reggie.service.DishFlavorService;
import com.lxz.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements  DishService{

    @Autowired
    private DishFlavorService dishFlavorService;

    // 因为在这一个方法中使用了两个表，因此需要这个注解
    @Transactional
    // 新增菜品，同时插入菜品数据和菜品口味数据，因此需要分别操作dish和dishflavor两个表
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到菜品表dish
        // dishDto继承自dish，因此可以直接传递进来
        this.save(dishDto);

        // 下述代码的作用是将dishid放进flavors列表中。这是由于，在数据进行封装的时候，没有将这个id封装进去，因此需要进行添加
        //获取菜品id
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        // .stream().map((item)类似于for循环，将菜品的id放进flavors中
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 保存菜品口味到口味表dish_flavor
        // dishDto中就包含了一个列表形式的flavor数据，也是因此需要使用saveBatch而不是save
        dishFlavorService.saveBatch(flavors);
    }

    // 根据id查询菜品信息和对应的口味信息
    public DishDto getWithFlavor(Long id) {
        DishDto dishDto = new DishDto();

        // 查询菜品基本信息，从dish表查询
        // this指当前的service
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish, dishDto);

        // 查询菜品口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表的数据
        this.updateById(dishDto);

        // 清理当前菜品的口味数据
        // 做清理的原因是一个dish_id在数据库中存了多条flavor的数据，
        //  如果发生了flavor数据的改变如数量减少等，更新就不能保证数据正确，因此要删除然后insert新的数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        // 添加当前提交的口味的数据到dishFlavor
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
