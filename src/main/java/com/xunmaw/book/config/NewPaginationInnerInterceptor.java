package com.xunmaw.book.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewPaginationInnerInterceptor extends PaginationInnerInterceptor {
    private DbType dbType;

    public NewPaginationInnerInterceptor(DbType dbType) {
        this.dbType = dbType;
    }

    @Override
    protected void handlerOverflow(IPage<?> page) {
        page.setCurrent(page.getCurrent()-1);
    }
}
