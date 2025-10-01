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

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.interfaces.WiSupport;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author miemie
 * @since 2018-12-12
 */
public interface Update<Mut, Children> extends WiSupport<Mut>, Serializable {

    default Children set(String column, Object value) {
        return set(true, column, value);
    }

    default Children set(String column, Object value, String mapping) {
        return set(true, column, value, mapping);
    }

    default Children set(boolean condition, String column, Object value) {
        return set(condition, column, value, null);
    }

    default Children set(boolean condition, String column, Object value, String mapping) {
        return set(condition, strCol2Segment(column), value, Objects.isNull(mapping) ? null : () -> mapping);
    }

    default Children set(Mut column, Object value) {
        return set(true, column, value);
    }

    default Children set(boolean condition, Mut column, Object value) {
        return set(condition, column, value, false);
    }

    default Children set(Mut column, Object value, boolean mapping) {
        return set(true, column, value, mapping);
    }

    default Children set(boolean condition, Mut column, Object value, boolean mapping) {
        return set(condition, convMut2ColSegment(column), value, mappingSupplier(mapping, column));
    }

    /**
     * 设置 更新 SQL 的 SET 片段
     *
     * @param condition 是否加入 set
     * @param column    字段
     * @param value     值
     * @param mapping   例: javaType=int,jdbcType=NUMERIC,typeHandler=xxx.xxx.MyTypeHandler
     * @return children
     */
    Children set(boolean condition, ISqlSegment column, Object value, Supplier<String> mapping);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children setSql(String setSql, Object... params) {
        return setSql(true, setSql, params);
    }

    /**
     * 设置 更新 SQL 的 SET 片段
     *
     * @param condition 执行条件
     * @param setSql    set sql
     *                  例1: setSql("id=1")
     *                  例2: setSql("dateColumn={0}", LocalDate.now())
     *                  例4: setSql("type={0,javaType=int,jdbcType=NUMERIC,typeHandler=xxx.xxx.MyTypeHandler}", "待处理字符串")
     * @return children
     */
    Children setSql(boolean condition, String setSql, Object... params);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children setIncrBy(String column, Number value) {
        return setIncrBy(true, column, value);
    }

    default Children setIncrBy(boolean condition, String column, Number value) {
        return setIncrBy(condition, strCol2Segment(column), value);
    }

    default Children setIncrBy(Mut column, Number value) {
        return setIncrBy(true, column, value);
    }

    default Children setIncrBy(boolean condition, Mut column, Number value) {
        return setIncrBy(condition, convMut2ColSegment(column), value);
    }

    /**
     * 字段自增变量 value 值
     *
     * @param condition 是否加入 set
     * @param column    字段
     * @param value     变量值 1 字段自增 + 1
     */
    Children setIncrBy(boolean condition, ISqlSegment column, Number value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children setDecrBy(String column, Number value) {
        return setDecrBy(true, column, value);
    }

    default Children setDecrBy(boolean condition, String column, Number value) {
        return setDecrBy(condition, strCol2Segment(column), value);
    }

    default Children setDecrBy(Mut column, Number value) {
        return setDecrBy(true, column, value);
    }

    default Children setDecrBy(boolean condition, Mut column, Number value) {
        return setDecrBy(condition, convMut2ColSegment(column), value);
    }

    /**
     * 字段自减变量 value 值
     *
     * @param condition 是否加入 set
     * @param column    字段
     * @param value     变量值 1 字段自减 - 1
     */
    Children setDecrBy(boolean condition, ISqlSegment column, Number value);

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 获取 更新 SQL 的 SET 片段
     */
    String getSqlSet();
}
