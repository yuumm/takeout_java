package com.lxz.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxz.reggie.common.CustomException;
import com.lxz.reggie.entity.Category;
import com.lxz.reggie.entity.Dish;
import com.lxz.reggie.entity.Setmeal;
import com.lxz.reggie.mapper.CategoryMapper;
import com.lxz.reggie.service.CategoryService;
import com.lxz.reggie.service.DishService;
import com.lxz.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 可以在service中利用mp构建自己想要的查询方法。

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    // 因为后面要查询dish的数据，所以这里要调用dish的service
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    // 根据id删除套餐的数据，并在删除前进行判断是否有菜品关联该信息。
    // 这里的id是套餐的id
    public void remove(Long id) {
        // 查询菜品是否关联了当前数据
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        // 获取查询到的数据的数量，并进行判断
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            // 说明有菜品关联了当前菜系的id，不能直接删除id，调用CustomException抛出异常
            throw new CustomException("当前菜系关联了菜品，不能删除");
        }

        // 查询套餐是否关联了当前数据
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        // 获取查询到的数据的数量，并进行判断
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            // 说明有套餐关联了当前菜系的id，不能直接删除id，抛出异常。
            // 有了异常就需要捕获，在全局异常处理GlobalExceptionHandler进行捕获
            throw new CustomException("当前菜系关联了套餐，不能删除");
        }

        // 正常删除分类
        super.removeById(id);
    }
}
