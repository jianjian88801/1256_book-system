package com.xunmaw.book.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user")
public class User {
    @TableId(value = "id")
    private Integer id;
    private String name;
    private String username;
    private String password;
    private String sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private String address;
    private String phone;
    private Integer flag;
}
