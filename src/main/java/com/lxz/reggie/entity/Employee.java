package com.lxz.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

// 员工类
// @Data提供类的get、set、equals、hashCode、toString等方法，就不用自己再去写这些方法了
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    // 身份证号码
    private String idNumber;

    private Integer status;

    // 下面几个字段都是每个数据表都有的数据，然后每次更新或者添加数据的时候都会调用
    // 为了减少工作量，可以添加TableField，然后mp可以实现自动填充

    // INSERT表示插入的时候填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // INSERT_UPDATE表示插入和更新的时候填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
