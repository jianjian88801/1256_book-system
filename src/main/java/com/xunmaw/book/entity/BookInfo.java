package com.xunmaw.book.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "book_info")
public class BookInfo implements Serializable {
    @TableId(value = "book_id")
    private Integer bookId;
    private String name;
    private String author;
    private String publish;
    private String isbn;
    private String introduction;
    private String language;
    private double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pubDate;
    private Integer  classId;
    private Integer  number;
}
