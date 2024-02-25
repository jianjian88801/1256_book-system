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
@TableName(value = "lend_list")
public class LendList{
    @TableId(value = "ser_num")
    private Integer serNum;
    private Integer bookId;
    private Integer readerId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lendDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date backDate;
    private Integer flag;
    private Integer lossflag;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lossDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    private BookInfo bookInfo;
}
