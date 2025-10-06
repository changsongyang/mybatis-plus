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
package com.baomidou.mybatisplus.core.conditions.interfaces;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 查询条件封装
 * <p>比较值</p>
 *
 * @author hubin miemie HCL
 * @since 2017-05-26
 */
public interface Compare<T, Children> extends WiSupport<T>, Serializable {

    /**
     * map 所有非空属性等于 =
     *
     * @param params map 类型的参数, key 是字段名, value 是字段值
     * @return children
     */
    default <V> Children allEq(Map<String, V> params) {
        return allEq(params, true);
    }

    /**
     * map 所有非空属性等于 =
     *
     * @param params      map 类型的参数, key 是字段名, value 是字段值
     * @param null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段
     * @return children
     */
    default <V> Children allEq(Map<String, V> params, boolean null2IsNull) {
        return allEq(true, params, null2IsNull);
    }

    /**
     * map 所有非空属性等于 =
     *
     * @param condition   执行条件
     * @param params      map 类型的参数, key 是字段名, value 是字段值
     * @param null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段
     * @return children
     */
    <V> Children allEq(boolean condition, Map<String, V> params, boolean null2IsNull);

    /**
     * 字段过滤接口，传入多参数时允许对参数进行过滤
     *
     * @param filter 返回 true 来允许字段传入比对条件中
     * @param params map 类型的参数, key 是字段名, value 是字段值
     * @return children
     */
    default <V> Children allEq(BiPredicate<String, V> filter, Map<String, V> params) {
        return allEq(filter, params, true);
    }

    /**
     * 字段过滤接口，传入多参数时允许对参数进行过滤
     *
     * @param filter      返回 true 来允许字段传入比对条件中
     * @param params      map 类型的参数, key 是字段名, value 是字段值
     * @param null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段
     * @return children
     */
    default <V> Children allEq(BiPredicate<String, V> filter, Map<String, V> params, boolean null2IsNull) {
        return allEq(true, filter, params, null2IsNull);
    }

    /**
     * 字段过滤接口，传入多参数时允许对参数进行过滤
     *
     * @param condition   执行条件
     * @param filter      返回 true 来允许字段传入比对条件中
     * @param params      map 类型的参数, key 是字段名, value 是字段值
     * @param null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段
     * @return children
     */
    <V> Children allEq(boolean condition, BiPredicate<String, V> filter, Map<String, V> params, boolean null2IsNull);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children eq(String column, Object value) {
        return eq(true, column, value);
    }

    default Children eq(String column, Object value, String mapping) {
        return eq(true, column, value, mapping);
    }

    default Children eq(boolean condition, String column, Object value) {
        return eq(condition, column, value, null);
    }

    default Children eq(boolean condition, String column, Object value, String mapping) {
        return eq(condition, strCol2Segment(column), value, () -> mapping);
    }

    default Children eq(SFunction<T, ?> column, Object value) {
        return eq(true, column, value, false);
    }

    default Children eq(SFunction<T, ?> column, Object value, boolean mapping) {
        return eq(true, column, value, mapping);
    }

    default Children eq(SFunction<T, ?> column, Object value, String mapping) {
        return eq(true, column, value, mapping);
    }

    default Children eq(boolean condition, SFunction<T, ?> column, Object value) {
        return eq(condition, column, value, false);
    }

    default Children eq(boolean condition, SFunction<T, ?> column, Object value, boolean mapping) {
        return eq(condition, convMut2ColSegment(column), value, mappingSupplier(mapping, column));
    }

    default Children eq(boolean condition, SFunction<T, ?> column, Object value, String mapping) {
        return eq(condition, convMut2ColSegment(column), value, () -> mapping);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @param mapping   mapping
     * @return children
     */
    Children eq(boolean condition, ISqlSegment column, Object value, Supplier<String> mapping);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children ne(String column, Object value) {
        return ne(true, column, value);
    }

    default Children ne(boolean condition, String column, Object value) {
        return ne(condition, strCol2Segment(column), value);
    }

    default Children ne(SFunction<T, ?> column, Object value) {
        return ne(true, column, value);
    }

    default Children ne(boolean condition, SFunction<T, ?> column, Object value) {
        return ne(condition, convMut2ColSegment(column), value);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children ne(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children gt(String column, Object value) {
        return gt(true, column, value);
    }

    default Children gt(boolean condition, String column, Object value) {
        return gt(condition, strCol2Segment(column), value);
    }

    default Children gt(SFunction<T, ?> column, Object value) {
        return gt(true, column, value);
    }

    default Children gt(boolean condition, SFunction<T, ?> column, Object value) {
        return gt(condition, convMut2ColSegment(column), value);
    }

    /**
     * 大于 &gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children gt(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children ge(String column, Object value) {
        return ge(true, column, value);
    }

    default Children ge(boolean condition, String column, Object value) {
        return ge(condition, strCol2Segment(column), value);
    }

    default Children ge(SFunction<T, ?> column, Object value) {
        return ge(true, column, value);
    }

    default Children ge(boolean condition, SFunction<T, ?> column, Object value) {
        return ge(condition, convMut2ColSegment(column), value);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children ge(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children lt(String column, Object value) {
        return lt(true, column, value);
    }

    default Children lt(boolean condition, String column, Object value) {
        return lt(condition, strCol2Segment(column), value);
    }

    default Children lt(SFunction<T, ?> column, Object value) {
        return lt(true, column, value);
    }

    default Children lt(boolean condition, SFunction<T, ?> column, Object value) {
        return lt(condition, convMut2ColSegment(column), value);
    }

    /**
     * 小于 &lt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children lt(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children le(String column, Object value) {
        return le(true, column, value);
    }

    default Children le(boolean condition, String column, Object value) {
        return le(condition, strCol2Segment(column), value);
    }

    default Children le(SFunction<T, ?> column, Object value) {
        return le(true, column, value);
    }

    default Children le(boolean condition, SFunction<T, ?> column, Object value) {
        return le(condition, convMut2ColSegment(column), value);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children le(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children between(String column, Object val1, Object val2) {
        return between(true, column, val1, val2);
    }

    default Children between(boolean condition, String column, Object val1, Object val2) {
        return between(condition, strCol2Segment(column), val1, val2);
    }

    default Children between(SFunction<T, ?> column, Object val1, Object val2) {
        return between(true, column, val1, val2);
    }

    default Children between(boolean condition, SFunction<T, ?> column, Object val1, Object val2) {
        return between(condition, convMut2ColSegment(column), val1, val2);
    }

    /**
     * BETWEEN 值1 AND 值2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     * @return children
     */
    Children between(boolean condition, ISqlSegment column, Object val1, Object val2);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children notBetween(String column, Object val1, Object val2) {
        return notBetween(true, column, val1, val2);
    }

    default Children notBetween(boolean condition, String column, Object val1, Object val2) {
        return notBetween(condition, strCol2Segment(column), val1, val2);
    }

    default Children notBetween(SFunction<T, ?> column, Object val1, Object val2) {
        return notBetween(true, column, val1, val2);
    }

    default Children notBetween(boolean condition, SFunction<T, ?> column, Object val1, Object val2) {
        return notBetween(condition, convMut2ColSegment(column), val1, val2);
    }

    /**
     * NOT BETWEEN 值1 AND 值2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     * @return children
     */
    Children notBetween(boolean condition, ISqlSegment column, Object val1, Object val2);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children like(String column, Object value) {
        return like(true, column, value);
    }

    default Children like(boolean condition, String column, Object value) {
        return like(condition, strCol2Segment(column), value);
    }

    default Children like(SFunction<T, ?> column, Object value) {
        return like(true, column, value);
    }

    default Children like(boolean condition, SFunction<T, ?> column, Object value) {
        return like(condition, convMut2ColSegment(column), value);
    }

    /**
     * LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children like(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children notLike(String column, Object value) {
        return notLike(true, column, value);
    }

    default Children notLike(boolean condition, String column, Object value) {
        return notLike(condition, strCol2Segment(column), value);
    }

    default Children notLike(SFunction<T, ?> column, Object value) {
        return notLike(true, column, value);
    }

    default Children notLike(boolean condition, SFunction<T, ?> column, Object value) {
        return notLike(condition, convMut2ColSegment(column), value);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children notLike(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children notLikeLeft(String column, Object value) {
        return notLikeLeft(true, column, value);
    }

    default Children notLikeLeft(boolean condition, String column, Object value) {
        return notLikeLeft(condition, strCol2Segment(column), value);
    }

    default Children notLikeLeft(SFunction<T, ?> column, Object value) {
        return notLikeLeft(true, column, value);
    }

    default Children notLikeLeft(boolean condition, SFunction<T, ?> column, Object value) {
        return notLikeLeft(condition, convMut2ColSegment(column), value);
    }

    /**
     * NOT LIKE '%值'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children notLikeLeft(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children notLikeRight(String column, Object value) {
        return notLikeRight(true, column, value);
    }

    default Children notLikeRight(boolean condition, String column, Object value) {
        return notLikeRight(condition, strCol2Segment(column), value);
    }

    default Children notLikeRight(SFunction<T, ?> column, Object value) {
        return notLikeRight(true, column, value);
    }

    default Children notLikeRight(boolean condition, SFunction<T, ?> column, Object value) {
        return notLikeRight(condition, convMut2ColSegment(column), value);
    }

    /**
     * NOT LIKE '值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children notLikeRight(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children likeLeft(String column, Object value) {
        return likeLeft(true, column, value);
    }

    default Children likeLeft(boolean condition, String column, Object value) {
        return likeLeft(condition, strCol2Segment(column), value);
    }

    default Children likeLeft(SFunction<T, ?> column, Object value) {
        return likeLeft(true, column, value);
    }

    default Children likeLeft(boolean condition, SFunction<T, ?> column, Object value) {
        return likeLeft(condition, convMut2ColSegment(column), value);
    }

    /**
     * LIKE '%值'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children likeLeft(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children likeRight(String column, Object value) {
        return likeRight(true, column, value);
    }

    default Children likeRight(boolean condition, String column, Object value) {
        return likeRight(condition, strCol2Segment(column), value);
    }

    default Children likeRight(SFunction<T, ?> column, Object value) {
        return likeRight(true, column, value);
    }

    default Children likeRight(boolean condition, SFunction<T, ?> column, Object value) {
        return likeRight(condition, convMut2ColSegment(column), value);
    }

    /**
     * LIKE '值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param value     值
     * @return children
     */
    Children likeRight(boolean condition, ISqlSegment column, Object value);

    /*----------------------------------------------------------------------------------------------------------------------------------------*/

    default Children isNull(String column) {
        return isNull(true, column);
    }

    default Children isNull(boolean condition, String column) {
        return isNull(condition, strCol2Segment(column));
    }

    default Children isNull(SFunction<T, ?> column) {
        return isNull(true, column);
    }

    default Children isNull(boolean condition, SFunction<T, ?> column) {
        return isNull(condition, convMut2ColSegment(column));
    }

    /**
     * 字段 IS NULL
     * <p>例: isNull(true, "name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    Children isNull(boolean condition, ISqlSegment column);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children isNotNull(String column) {
        return isNotNull(true, column);
    }

    default Children isNotNull(boolean condition, String column) {
        return isNotNull(condition, strCol2Segment(column));
    }

    default Children isNotNull(SFunction<T, ?> column) {
        return isNotNull(true, column);
    }

    default Children isNotNull(boolean condition, SFunction<T, ?> column) {
        return isNotNull(condition, convMut2ColSegment(column));
    }

    /**
     * 字段 IS NOT NULL
     * <p>例: isNotNull(true, "name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    Children isNotNull(boolean condition, ISqlSegment column);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children in(String column, Collection<?> coll) {
        return in(true, column, coll);
    }

    default Children in(boolean condition, String column, Collection<?> coll) {
        return in(condition, strCol2Segment(column), coll);
    }

    default Children in(SFunction<T, ?> column, Collection<?> coll) {
        return in(true, column, coll);
    }

    default Children in(boolean condition, SFunction<T, ?> column, Collection<?> coll) {
        return in(condition, convMut2ColSegment(column), coll);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     * <p>例: in(true, "id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * <li> 注意！当集合为 空或null 时, sql会拼接为：WHERE (字段名 IN ()), 执行时报错</li>
     * <li> 若要在特定条件下不拼接, 可在 condition 条件中判断 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    Children in(boolean condition, ISqlSegment column, Collection<?> coll);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children in(String column, Object... values) {
        return in(true, column, values);
    }

    default Children in(boolean condition, String column, Object... values) {
        return in(condition, strCol2Segment(column), values);
    }

    default Children in(SFunction<T, ?> column, Object... values) {
        return in(true, column, values);
    }

    default Children in(boolean condition, SFunction<T, ?> column, Object... values) {
        return in(condition, convMut2ColSegment(column), values);
    }

    /**
     * 字段 IN (v0, v1, ...)
     * <p>例: in(true, "id", 1, 2, 3, 4, 5)</p>
     *
     * <li> 注意！当数组为 空或null 时, sql会拼接为：WHERE (字段名 IN ()), 执行时报错</li>
     * <li> 若要在特定条件下不拼接, 可在 condition 条件中判断 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    Children in(boolean condition, ISqlSegment column, Object... values);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children notIn(String column, Collection<?> coll) {
        return notIn(true, column, coll);
    }

    default Children notIn(SFunction<T, ?> column, Collection<?> coll) {
        return notIn(true, column, coll);
    }

    default Children notIn(boolean condition, SFunction<T, ?> column, Collection<?> coll) {
        return notIn(condition, convMut2ColSegment(column), coll);
    }

    default Children notIn(boolean condition, String column, Collection<?> coll) {
        return notIn(condition, strCol2Segment(column), coll);
    }

    /**
     * 字段 NOT IN (value.get(0), value.get(1), ...)
     * <p>例: notIn(true, "id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * <li> 注意！当集合为 空或null 时, sql会拼接为：WHERE (字段名 NOT IN ()), 执行时报错</li>
     * <li> 若要在特定条件下不拼接, 可在 condition 条件中判断 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    Children notIn(boolean condition, ISqlSegment column, Collection<?> coll);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children notIn(String column, Object... values) {
        return notIn(true, column, values);
    }

    default Children notIn(boolean condition, String column, Object... values) {
        return notIn(condition, strCol2Segment(column), values);
    }

    default Children notIn(SFunction<T, ?> column, Object... values) {
        return notIn(true, column, values);
    }

    default Children notIn(boolean condition, SFunction<T, ?> column, Object... values) {
        return notIn(condition, convMut2ColSegment(column), values);
    }

    /**
     * 字段 NOT IN (v0, v1, ...)
     * <p>例: notIn(true, "id", 1, 2, 3, 4, 5)</p>
     *
     * <li> 注意！当数组为 空或null 时, sql会拼接为：WHERE (字段名 NOT IN ()), 执行时报错</li>
     * <li> 若要在特定条件下不拼接, 可在 condition 条件中判断 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    Children notIn(boolean condition, ISqlSegment column, Object... values);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children eqSql(String column, String sql) {
        return eqSql(true, column, sql);
    }

    default Children eqSql(boolean condition, String column, String sql) {
        return eqSql(condition, strCol2Segment(column), sql);
    }

    default Children eqSql(SFunction<T, ?> column, String sql) {
        return eqSql(true, column, sql);
    }

    default Children eqSql(boolean condition, SFunction<T, ?> column, String sql) {
        return eqSql(condition, convMut2ColSegment(column), sql);
    }

    /**
     * 字段 EQ ( sql语句 )
     * <p>!! sql 注入方式的 eq 方法 !!</p>
     * <p>例1: eqSql("id", "1")</p>
     * <p>例2: eqSql("id", "select MAX(id) from table")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param sql       sql语句
     * @return children
     * @since 3.5.6
     */
    Children eqSql(boolean condition, ISqlSegment column, String sql);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children inSql(String column, String sql) {
        return inSql(true, column, sql);
    }

    default Children inSql(boolean condition, String column, String sql) {
        return inSql(condition, strCol2Segment(column), sql);
    }

    default Children inSql(SFunction<T, ?> column, String sql) {
        return inSql(true, column, sql);
    }

    default Children inSql(boolean condition, SFunction<T, ?> column, String sql) {
        return inSql(condition, convMut2ColSegment(column), sql);
    }

    /**
     * 字段 IN ( sql语句 )
     * <p>!! sql 注入方式的 in 方法 !!</p>
     * <p>例1: inSql(true, "id", "1")</p>
     * <p>例2: inSql(true, "id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param sql       sql语句
     * @return children
     */
    Children inSql(boolean condition, ISqlSegment column, String sql);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children gtSql(String column, String sql) {
        return gtSql(true, column, sql);
    }

    default Children gtSql(boolean condition, String column, String sql) {
        return gtSql(condition, strCol2Segment(column), sql);
    }

    default Children gtSql(SFunction<T, ?> column, String sql) {
        return gtSql(true, column, sql);
    }

    default Children gtSql(boolean condition, SFunction<T, ?> column, String sql) {
        return gtSql(condition, convMut2ColSegment(column), sql);
    }

    /**
     * 字段 &gt; ( sql语句 )
     * <p>例1: gtSql("id", "1")</p>
     * <p>例1: gtSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param sql       sql语句
     * @return children
     */
    Children gtSql(boolean condition, ISqlSegment column, String sql);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children geSql(String column, String sql) {
        return geSql(true, column, sql);
    }

    default Children geSql(SFunction<T, ?> column, String sql) {
        return geSql(true, column, sql);
    }

    default Children geSql(boolean condition, SFunction<T, ?> column, String sql) {
        return geSql(condition, convMut2ColSegment(column), sql);
    }

    default Children geSql(boolean condition, String column, String sql) {
        return geSql(condition, strCol2Segment(column), sql);
    }

    /**
     * 字段 >= ( sql语句 )
     * <p>例1: geSql(true, "id", "1")</p>
     * <p>例1: geSql(true, "id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param sql       sql语句
     * @return children
     */
    Children geSql(boolean condition, ISqlSegment column, String sql);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children ltSql(String column, String sql) {
        return ltSql(true, column, sql);
    }

    default Children ltSql(boolean condition, String column, String sql) {
        return ltSql(condition, strCol2Segment(column), sql);
    }

    default Children ltSql(SFunction<T, ?> column, String sql) {
        return ltSql(true, column, sql);
    }

    default Children ltSql(boolean condition, SFunction<T, ?> column, String sql) {
        return ltSql(condition, convMut2ColSegment(column), sql);
    }

    /**
     * 字段 &lt; ( sql语句 )
     * <p>例1: ltSql(true, "id", "1")</p>
     * <p>例1: ltSql(true , "id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param sql       sql语句
     * @return children
     */
    Children ltSql(boolean condition, ISqlSegment column, String sql);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children leSql(String column, String sql) {
        return leSql(true, column, sql);
    }

    default Children leSql(boolean condition, String column, String sql) {
        return leSql(condition, strCol2Segment(column), sql);
    }

    default Children leSql(SFunction<T, ?> column, String sql) {
        return leSql(true, column, sql);
    }

    default Children leSql(boolean condition, SFunction<T, ?> column, String sql) {
        return leSql(condition, convMut2ColSegment(column), sql);
    }

    /**
     * 字段 <= ( sql语句 )
     * <p>例1: leSql(true, "id", "1")</p>
     * <p>例1: leSql(true ,"id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param sql       sql语句
     * @return children
     */
    Children leSql(boolean condition, ISqlSegment column, String sql);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children notInSql(String column, String sql) {
        return notInSql(true, column, sql);
    }

    default Children notInSql(boolean condition, String column, String sql) {
        return notInSql(condition, strCol2Segment(column), sql);
    }

    default Children notInSql(SFunction<T, ?> column, String sql) {
        return notInSql(true, column, sql);
    }

    default Children notInSql(boolean condition, SFunction<T, ?> column, String sql) {
        return notInSql(condition, convMut2ColSegment(column), sql);
    }

    /**
     * 字段 NOT IN ( sql语句 )
     * <p>!! sql 注入方式的 not in 方法 !!</p>
     * <p>例1: notInSql(true, "id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例2: notInSql(true, "id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param sql       sql语句 ---> 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    Children notInSql(boolean condition, ISqlSegment column, String sql);

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy(true, () -> "id")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    Children groupBy(boolean condition, ISqlSegment column);

    default Children groupBy(String column, String... columns) {
        return groupBy(true, column, columns);
    }

    default Children groupBy(boolean condition, String column, String... columns) {
        return groupBy(condition, column, ArrayUtils.isNotEmpty(columns) ? Arrays.asList(columns) : null);
    }

    default Children groupBy(Collection<String> columns) {
        return groupBy(true, columns);
    }

    default Children groupBy(boolean condition, Collection<String> columns) {
        return groupBy(condition, () -> columns.stream().filter(Objects::nonNull).collect(Collectors.joining(StringPool.COMMA)));
    }

    default Children groupBy(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return groupBy(true, column, columns);
    }

    default Children groupBy(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return groupBy(condition, column, ArrayUtils.isNotEmpty(columns) ? Arrays.asList(columns) : null);
    }

    default Children groupBy(boolean condition, String column, Collection<String> columns) {
        return groupBy(condition, strPeek(column, columns));
    }

    default Children groupBy(boolean condition, SFunction<T, ?> column, Collection<SFunction<T, ?>> columns) {
        return groupBy(condition, mutPeek(column, columns));
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children orderByAsc(Collection<String> columns) {
        return orderBy(true, true, columns);
    }

    default Children orderByAsc(boolean condition, Collection<String> columns) {
        return orderBy(condition, true, columns);
    }

    default Children orderByAsc(String column, String... columns) {
        return orderBy(true, true, column, columns);
    }

    default Children orderByAsc(boolean condition, String column, String... columns) {
        return orderBy(condition, true, column, columns);
    }

    default Children orderByAsc(boolean condition, String column, Collection<String> columns) {
        return orderBy(condition, true, column, columns);
    }

    default Children orderByAsc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return orderBy(true, true, column, columns);
    }

    default Children orderByAsc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return orderBy(condition, true, column, columns);
    }

    default Children orderByAsc(boolean condition, SFunction<T, ?> column, Collection<SFunction<T, ?>> columns) {
        return orderBy(condition, true, column, columns);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children orderByDesc(Collection<String> columns) {
        return orderBy(true, false, columns);
    }

    default Children orderByDesc(boolean condition, Collection<String> columns) {
        return orderBy(condition, false, columns);
    }

    default Children orderByDesc(String column, String... columns) {
        return orderBy(true, false, column, columns);
    }

    default Children orderByDesc(boolean condition, String column, String... columns) {
        return orderBy(condition, false, column, columns);
    }

    default Children orderByDesc(boolean condition, String column, Collection<String> columns) {
        return orderBy(condition, false, column, columns);
    }

    default Children orderByDesc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return orderBy(true, false, column, columns);
    }

    default Children orderByDesc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return orderBy(condition, false, column, columns);
    }

    default Children orderByDesc(boolean condition, SFunction<T, ?> column, Collection<SFunction<T, ?>> columns) {
        return orderBy(condition, false, column, columns);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 排序：ORDER BY 字段, ...
     * <p>例: orderBy(true, Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param isAsc     是否是 ASC 排序
     * @param column    字段
     * @return children
     */
    Children orderBy(boolean condition, boolean isAsc, ISqlSegment column);

    default Children orderBy(boolean condition, boolean isAsc, Collection<String> columns) {
        return orderBy(condition, isAsc, () -> columns.stream().filter(Objects::nonNull).collect(Collectors.joining(StringPool.COMMA)));
    }

    default Children orderBy(boolean condition, boolean isAsc, String column, String... columns) {
        return orderBy(condition, isAsc, column, ArrayUtils.isNotEmpty(columns) ? Arrays.asList(columns) : null);
    }

    default Children orderBy(boolean condition, boolean isAsc, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return orderBy(condition, isAsc, column, ArrayUtils.isNotEmpty(columns) ? Arrays.asList(columns) : null);
    }

    default Children orderBy(boolean condition, boolean isAsc, String column, Collection<String> columns) {
        return orderBy(condition, isAsc, strPeek(column, columns));
    }

    default Children orderBy(boolean condition, boolean isAsc, SFunction<T, ?> column, Collection<SFunction<T, ?>> columns) {
        return orderBy(condition, isAsc, mutPeek(column, columns));
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children having(String sqlHaving, Object... params) {
        return having(true, sqlHaving, params);
    }

    /**
     * HAVING ( sql语句 )
     * <p>例1: having(true, "sum(age) &gt; 10")</p>
     * <p>例2: having(true, "sum(age) &gt; {0}", 10)</p>
     *
     * @param condition 执行条件
     * @param sqlHaving sql 语句
     * @param params    参数数组
     * @return children
     */
    Children having(boolean condition, String sqlHaving, Object... params);

    /*----------------------------------------------------------------------------------------------------------------*/

    default Children func(Consumer<Children> consumer) {
        return func(true, consumer);
    }

    /**
     * 消费函数
     *
     * @param condition 执行条件
     * @param consumer  消费函数
     * @return children
     */
    Children func(boolean condition, Consumer<Children> consumer);
}
