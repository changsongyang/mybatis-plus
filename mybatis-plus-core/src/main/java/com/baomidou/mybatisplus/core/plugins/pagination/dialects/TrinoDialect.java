package com.baomidou.mybatisplus.core.plugins.pagination.dialects;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.plugins.pagination.DialectModel;

/**
 * Trino 数据库分页语句组装实现
 *
 * @author hushunbo
 * @since 2023-10-06
 */
public class TrinoDialect implements IDialect {

    @Override
    public DialectModel buildPaginationSql(String originalSql, IPage<?> page) {
        long offset = page.offset();
        long limit = page.getSize();
        StringBuilder sql = new StringBuilder(originalSql);
        if (offset != 0L) {
            sql.append(" OFFSET ").append(FIRST_MARK);
            sql.append(" LIMIT ").append(SECOND_MARK);
            return new DialectModel(sql.toString(), offset, limit).setConsumerChain();
        } else {
            sql.append(" LIMIT ").append(FIRST_MARK);
            return new DialectModel(sql.toString(), limit).setConsumer(true);
        }
    }
}
