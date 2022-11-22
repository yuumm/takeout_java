package com.lxz.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxz.reggie.common.CustomException;
import com.lxz.reggie.dto.SetmealDto;
import com.lxz.reggie.entity.Setmeal;
import com.lxz.reggie.entity.SetmealDish;
import com.lxz.reggie.mapper.SetmealMapper;
import com.lxz.reggie.service.SetmealDishService;
import com.lxz.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    // 因为要在这个service里面操作setmealDishService的表
    @Autowired
    private SetmealDishService setmealDishService;

    // 新增套餐，同时保存套餐和菜品的关联关系
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息，对setmeal表进行insert操作
        // SetmealDto继承了Setmeal类，因此下述方法可以直接操作Setmeal表
        this.save(setmealDto);

        /*该方法逻辑梳理
        * 数据库中两个表：setmeal, setmealDish, 分别存储的是套餐信息，以及某个套餐包含了哪些菜品的对应信息
        * 前端返回的数据是一个SetmealDto类型的数据，其中的setmealDishes字段是一个存储了该套餐所包含的菜品的对应的信息
        *   这是一个列表数据
        * setmealId是套餐的id，前端传进来的时候是没有这个id的，只有在保存的时候会生成一个随机的id，
        *   因此，上述this.save方法调用后才有这个id。
        * */
        /*stream流的逻辑梳理
        我们要在后面要对setmealDish进行保存，但是前端只传进来了一个以setmealDish为数据类型的setmealDishs列表
            因此我们需要将setmealDishs转变成一个setmealDish数据，这个时候就可以使用stream进行操作。
        stream的主要逻辑是类似于一个循环将数据放进item，这个时候的item就是一个一个的setmealDish类型的数据，然后返回item。
        在这里的代码中间进行了一个处理，就是对setmealDish类中的setmealId进行赋值，
            因为存储数据的时候需要有setmealId，因此当列表中的数据一条一条被拿出来的时候就可以对这些数据进行赋值了
        * */
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 保存套餐和菜品的信息，对setmeal_dish表进行insert操作
        // 因为一次要操作多条数据，所以使用了saveBatch
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    // 保证数据的一致性
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态（只有在套餐被挺瘦的情况下才能删除）
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        /*
        * 这里我们要构造的sql语句为（其中count表示获取查询数据的数量，
        *   因为我们只需要判断有没有查出数据，所以判断count是否为0就可以了）
        * select count(*) from Setmeal where id in (1,2,3) and status = 1;
        * */
        queryWrapper.in(Setmeal::getId, ids)
                .eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        // 若不能删除则抛出错误
        if (count > 0) {
            // CustomException是我们自定义的抛出错误的方法
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        // 删除套餐数据(Setmeal)
        // 当要删除的id是以列表的形式传递进来的时候，要用removeByIds
        this.removeByIds(ids);

        // 删除菜品数据(SetmealDish)
        // 先通过setmeal的id查询到对应的id，然后再删除菜品数据
        // delete from * where setmealId in (1,2,3);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(lambdaQueryWrapper);
    }


}
