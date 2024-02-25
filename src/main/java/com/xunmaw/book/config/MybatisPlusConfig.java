package com.xunmaw.book.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        NewPaginationInnerInterceptor newPaginationInnerInterceptor = new NewPaginationInnerInterceptor(DbType.MYSQL);
        newPaginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(newPaginationInnerInterceptor);
        return interceptor;
    }
}
