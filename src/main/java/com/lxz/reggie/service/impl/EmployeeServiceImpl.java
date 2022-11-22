package com.lxz.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxz.reggie.entity.Employee;
import com.lxz.reggie.mapper.EmployeeMapper;
import com.lxz.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

// Service注解由spring使用
@Service
// ServiceImpl是一个由mybatisplus提供的接口。需要传入两个泛型，分别是mapper的和pojo类的。
//  继承这个接口之后就会自动实现一些增删改查的方法
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
