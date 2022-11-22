package com.lxz.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxz.reggie.entity.Employee;

// IService也是由mybatisplus自带的一个接口，这个接口里面有一些例如save（增添数据）等方法
public interface EmployeeService extends IService<Employee> {

}
