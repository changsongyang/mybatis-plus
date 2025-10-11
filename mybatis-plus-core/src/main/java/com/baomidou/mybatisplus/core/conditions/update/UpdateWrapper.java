/*
 * Copyright (c) 2011-2025, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.mybatisplus.core.conditions.update;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;

import java.math.BigDecimal;
import java.util.function.Supplier;

/**
 * Update 条件封装
 *
 * @author hubin miemie HCL
 * @since 2018-05-30
 */
public class UpdateWrapper<T> extends AbstractWrapper<T, UpdateWrapper<T>> implements Update<T, UpdateWrapper<T>> {
    private static final long serialVersionUID = -1L;

    public UpdateWrapper() {
        // 如果无参构造函数，请注意实体 NULL 情况 SET 必须有否则 SQL 异常
        super();
    }

    public UpdateWrapper(T entity) {
        setEntity(entity);
    }

    public UpdateWrapper(Class<T> entityClass) {
        setEntityClass(entityClass);
    }

    @Override
    public String getSqlSet() {
        if (CollectionUtils.isEmpty(selectBodyOrSetSql)) {
            return null;
        }
        return String.join(Constants.COMMA, selectBodyOrSetSql);
    }

    @Override
    public UpdateWrapper<T> set(boolean condition, ISqlSegment column, Object value, Supplier<String> mapping) {
        return maybeDo(condition, () -> {
            String sql = formatParam(value, mapping);
            selectBodyOrSetSql.add(column.getSqlSegment() + Constants.EQUALS + sql);
        });
    }

    @Override
    public UpdateWrapper<T> setSql(boolean condition, String setSql, Object... params) {
        return maybeDo(condition, () -> selectBodyOrSetSql.add(formatSqlMaybeWithParam(setSql, params)));
    }

    @Override
    public UpdateWrapper<T> setIncrBy(boolean condition, ISqlSegment column, Number value) {
        return maybeDo(condition, () -> {
            String tc = column.getSqlSegment();
            selectBodyOrSetSql.add(String.format("%s=%s + %s", tc, tc, value instanceof BigDecimal ? ((BigDecimal) value).toPlainString() : value));
        });
    }

    @Override
    public UpdateWrapper<T> setDecrBy(boolean condition, ISqlSegment column, Number value) {
        return maybeDo(condition, () -> {
            String tc = column.getSqlSegment();
            selectBodyOrSetSql.add(String.format("%s=%s - %s", tc, tc, value instanceof BigDecimal ? ((BigDecimal) value).toPlainString() : value));
        });
    }
}
