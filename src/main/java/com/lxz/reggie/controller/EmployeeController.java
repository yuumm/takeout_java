package com.lxz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxz.reggie.common.R;
import com.lxz.reggie.entity.Employee;
import com.lxz.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http11.HttpOutputBuffer;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.DigestException;
import java.time.LocalDateTime;

// Slf4j可以在控制台增加一些日志
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    public EmployeeService employeeService;


//    登陆功能
    @PostMapping("/login")
    // 前端传过来的数据是json格式，所以要用RequestBody。
    // 前端传入controller的参数少于接收实体类的所有参数时，需要加上@RequestBody注解给controller中的接收类，
    //  这样才可以进行正常访问。此时的RequestBody会将json中的数据自动匹配到后端的实体类中
    // request参数的主要作用是get一个session。因为当用户登陆之后要将用户的id存入session。
    //  此时就可以将employee中的数据存入request获取的session中
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
//        1、将前端提交的密码进行md5加密
        String password = employee.getPassword();
        // getBytes()表示将password转换为byte数据
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//        2、根据用户名查询数据库
        // LambdaQueryWrapper是mybatisplys中，一个按条件查询的接口。
        // <Employee>传入泛型是为了能够在下条代码实现这样的Employee::getUsername语句，
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // eq表示equal，表示两者相等，这是LambdaQueryWrapper中提供的条件查询的方法
        // 下面这句话相当于：select * from xxx where employee.getUsername() = Employee::getUsername
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        // getOne表示查询一个，是自带的方法。emp中保存查询出来的Employee类的数据。employee保存前端传进来的Employee类的数据
        Employee emp = employeeService.getOne(queryWrapper);

//        3、如果没有查询到数据则返回登录失败
        if (emp == null) {
            return R.error("用户不存在，登录失败");
        }

//        4、比对密码是否一致，若为否，则返回登录失败
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误，登录失败");
        }

//        5、查看员工状态，若为禁用，则返回登录失败
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

//        6、登录成功，将用户的id存入session，并返回登录成功的结果
        // session是以一个键值对的方式存入
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    // 由于后面要操作session，所以传入了request对象
    // 由于前端要的数据仅仅是一个String的数据，所以返回对象是一个String
    public R<String> logout(HttpServletRequest request) {
        // 清理session中当前登录员工的id信息
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    // 新增员工
    @PostMapping
    // request用于获取session中的用户的id
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        // 因为接收过来的数据是一个json，因此要tostring一下
        log.info("新增员工，员工信息: {}", employee.toString());

        // 给一个初始密码123456，并使用md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        // 写入创建时间。LocalDateTime.now()表示获取当前时间
//        employee.setCreateTime(LocalDateTime.now());
        // 写入更新时间
//        employee.setUpdateTime(LocalDateTime.now());

        // 获取当前登录用户的id
        // 由于前面emp.getId方法存入session的id是一个Long类型的数据，因此这里的变量也应该是Long类型
        // getAttribute取出来的数据是object类型
        Long empId = (Long) request.getSession().getAttribute("employee");

        // 设置createuser
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    // 分页查询的方法
    // Page是mp定义的一个类，主要作用就是返回分页查询类型的数据。前端要求返回的这个类型的数据。
    // page和pageSize是前端传进来的数据，值分别是1和10。当使用查询功能的时候，会使用姓名进行查询，此时还会传进来一个name的参数
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 构造分页构造器（通过page和pageSize来告诉mp要查几页，每页多少数据）
        // Page是mp内部的一个类，可以辅助构建分页构造器
        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器（主要是用户输入name进行条件查询的时候要用到）
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        if (name != null) {
            queryWrapper.like(Employee::getName, name);
        }

        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // queryWrapper在前面.like之类的，就是凑sql语句，然后放到这里面进行查询
        // page就是mp自带的分页查询的方法，不需要返回值，查出来的数据会自动赋值到pageInfo中
        employeeService.page(pageInfo, queryWrapper);
        //执行查询
        return R.success(pageInfo);
    }

    // 根据前端传进来的id，修改员工信息
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        Long empId = (Long)request.getSession().getAttribute("employee");
        System.out.println(empId);
//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        log.info(employee.toString());
        return R.success("员工信息修改成功");
    }

    // 通过id对员工数据进行查询。前端的操作是将id写入url中，然后向后端请求，因此要将url中的ID拿取出来
    // /{id}表示id是一个需要获取的参数，PathVariable表示后面的参数的值从url的地址中获取
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee!=null) {
            return R.success(employee);
        }
        return R.error("没有查询到员工信息...");
    }
}
