package com.lxz.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxz.reggie.dto.SetmealDto;
import com.lxz.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    // 保存套餐
    public void saveWithDish(SetmealDto setmealDto);

    // 删除套餐，同时要删除套餐对应的菜品数据
    public void removeWithDish(List<Long> ids);
}
