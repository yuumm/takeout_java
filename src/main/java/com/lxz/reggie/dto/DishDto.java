package com.lxz.reggie.dto;

import com.lxz.reggie.entity.Dish;
import com.lxz.reggie.entity.DishFlavor;
import com.lxz.reggie.entity.Dish;
import com.lxz.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

// dto是一个用于前端和后端数据传输的一个对象。
// 主要是由于，前端有时候传输到后端的数据和实体类中的不太一样，这会导致数据匹配不上，因此要用到这个对象。

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
