package com.lxz.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxz.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
// BaseMapper是一个基于mybatisplus的类，<>是一个泛型，里面接收实体类
// 这个BaseMapper类已经封装了很多增删改查的数据库的操作，所以不需要我们自己去写，可以直接调用
public interface EmployeeMapper extends BaseMapper<Employee> {
}
