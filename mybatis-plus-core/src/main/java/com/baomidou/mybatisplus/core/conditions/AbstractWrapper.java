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
package com.baomidou.mybatisplus.core.conditions;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.conditions.interfaces.Nested;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.*;
import static com.baomidou.mybatisplus.core.enums.WrapperKeyword.APPLY;
import static java.util.stream.Collectors.joining;

/**
 * 查询条件封装
 *
 * @author hubin miemie HCL
 * @since 2017-05-26
 */
@SuppressWarnings({"unchecked", "serial"})
public abstract class AbstractWrapper<T, Mut, Children extends AbstractWrapper<T, Mut, Children>> extends Wrapper<T>
    implements Compare<Mut, Children>, Nested<AbstractWrapper<T, Mut, Children>, Children>, Join<Children>, SelfChildren<Children> {
    private WrapperNestedContext<T> context;
    protected MergeSegments expression = new MergeSegments();
    protected SharedString lastSql = SharedString.emptyString();
    /**
     * SQL注释
     */
    protected SharedString sqlComment = SharedString.emptyString();
    /**
     * SQL起始语句
     */
    protected SharedString sqlFirst = SharedString.emptyString();
    protected List<String> selectBodyOrSetSql = new ArrayList<>();

    public AbstractWrapper() {
    }

    @Override
    public Children selfOrChildren() {
        return (Children) this;
    }

    @Override
    public T getEntity() {
        return getContext().getEntity();
    }

    public Children setContext(WrapperNestedContext<T> context) {
        Assert.isNull(this.context, "context is initialized");
        this.context = context;
        return selfOrChildren();
    }

    public Children setEntity(T entity) {
        getContext().setEntity(entity);
        return selfOrChildren();
    }

    public Children setEntityClass(Class<T> entityClass) {
        if (entityClass != null) {
            getContext().setEntityClass(entityClass);
        }
        return selfOrChildren();
    }

    @Override
    public <V> Children allEq(boolean condition, Map<String, V> params, boolean null2IsNull) {
        if (condition && CollectionUtils.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                if (StringUtils.checkValNotNull(v)) {
                    eq(k, v);
                } else {
                    if (null2IsNull) {
                        isNull(k);
                    }
                }
            });
        }
        return selfOrChildren();
    }

    @Override
    public <V> Children allEq(boolean condition, BiPredicate<String, V> filter, Map<String, V> params, boolean null2IsNull) {
        if (condition && CollectionUtils.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                if (filter.test(k, v)) {
                    if (StringUtils.checkValNotNull(v)) {
                        eq(k, v);
                    } else {
                        if (null2IsNull) {
                            isNull(k);
                        }
                    }
                }
            });
        }
        return selfOrChildren();
    }

    @Override
    public Children eq(boolean condition, ISqlSegment column, Object val, Supplier<String> mapping) {
        return addCondition(condition, column, EQ, val, mapping);
    }

    @Override
    public Children ne(boolean condition, ISqlSegment column, Object val) {
        return addCondition(condition, column, NE, val, null);
    }

    @Override
    public Children gt(boolean condition, ISqlSegment column, Object val) {
        return addCondition(condition, column, GT, val, null);
    }

    @Override
    public Children ge(boolean condition, ISqlSegment column, Object val) {
        return addCondition(condition, column, GE, val, null);
    }

    @Override
    public Children lt(boolean condition, ISqlSegment column, Object val) {
        return addCondition(condition, column, LT, val, null);
    }

    @Override
    public Children le(boolean condition, ISqlSegment column, Object val) {
        return addCondition(condition, column, LE, val, null);
    }

    @Override
    public Children like(boolean condition, ISqlSegment column, Object val) {
        return likeValue(condition, LIKE, column, val, SqlLike.DEFAULT);
    }

    @Override
    public Children notLike(boolean condition, ISqlSegment column, Object val) {
        return likeValue(condition, NOT_LIKE, column, val, SqlLike.DEFAULT);
    }

    @Override
    public Children likeLeft(boolean condition, ISqlSegment column, Object val) {
        return likeValue(condition, LIKE, column, val, SqlLike.LEFT);
    }

    @Override
    public Children likeRight(boolean condition, ISqlSegment column, Object val) {
        return likeValue(condition, LIKE, column, val, SqlLike.RIGHT);
    }

    @Override
    public Children notLikeLeft(boolean condition, ISqlSegment column, Object val) {
        return likeValue(condition, NOT_LIKE, column, val, SqlLike.LEFT);
    }

    @Override
    public Children notLikeRight(boolean condition, ISqlSegment column, Object val) {
        return likeValue(condition, NOT_LIKE, column, val, SqlLike.RIGHT);
    }

    @Override
    public Children between(boolean condition, ISqlSegment column, Object val1, Object val2) {
        return maybeDo(condition, () -> appendSqlSegments(column, BETWEEN,
            () -> formatParam(val1, null), AND, () -> formatParam(val2, null)));
    }

    @Override
    public Children notBetween(boolean condition, ISqlSegment column, Object val1, Object val2) {
        return maybeDo(condition, () -> appendSqlSegments(column, NOT_BETWEEN,
            () -> formatParam(val1, null), AND, () -> formatParam(val2, null)));
    }

    @Override
    public Children and(boolean condition, Consumer<AbstractWrapper<T, Mut, Children>> consumer) {
        return and(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public Children or(boolean condition, Consumer<AbstractWrapper<T, Mut, Children>> consumer) {
        return or(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public Children nested(boolean condition, Consumer<AbstractWrapper<T, Mut, Children>> consumer) {
        return addNestedCondition(condition, consumer);
    }

    @Override
    public Children not(boolean condition, Consumer<AbstractWrapper<T, Mut, Children>> consumer) {
        return not(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public Children or(boolean condition) {
        return maybeDo(condition, () -> appendSqlSegments(OR));
    }

    @Override
    public Children apply(boolean condition, String applySql, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(APPLY, () -> formatSqlMaybeWithParam(applySql, values)));
    }

    @Override
    public Children last(boolean condition, String lastSql) {
        if (condition) {
            this.lastSql.setStringValue(StringPool.SPACE + lastSql);
        }
        return selfOrChildren();
    }

    @Override
    public Children comment(boolean condition, String comment) {
        if (condition) {
            this.sqlComment.setStringValue(comment);
        }
        return selfOrChildren();
    }

    @Override
    public Children first(boolean condition, String firstSql) {
        if (condition) {
            this.sqlFirst.setStringValue(firstSql);
        }
        return selfOrChildren();
    }

    @Override
    public Children exists(boolean condition, String existsSql, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(EXISTS,
            () -> String.format("(%s)", formatSqlMaybeWithParam(existsSql, values))));
    }

    @Override
    public Children notExists(boolean condition, String existsSql, Object... values) {
        return not(condition).exists(condition, existsSql, values);
    }

    @Override
    public Children isNull(boolean condition, ISqlSegment column) {
        return maybeDo(condition, () -> appendSqlSegments(column, IS_NULL));
    }

    @Override
    public Children isNotNull(boolean condition, ISqlSegment column) {
        return maybeDo(condition, () -> appendSqlSegments(column, IS_NOT_NULL));
    }

    @Override
    public Children in(boolean condition, ISqlSegment column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(column, IN, inExpression(coll)));
    }

    @Override
    public Children in(boolean condition, ISqlSegment column, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(column, IN, inExpression(values)));
    }

    @Override
    public Children notIn(boolean condition, ISqlSegment column, Collection<?> coll) {
        return maybeDo(condition, () -> appendSqlSegments(column, NOT_IN, inExpression(coll)));
    }

    @Override
    public Children notIn(boolean condition, ISqlSegment column, Object... values) {
        return maybeDo(condition, () -> appendSqlSegments(column, NOT_IN, inExpression(values)));
    }

    @Override
    public Children eqSql(boolean condition, ISqlSegment column, String sql) {
        return maybeDo(condition, () -> appendSqlSegments(column, EQ,
            () -> String.format("(%s)", sql)));
    }

    @Override
    public Children inSql(boolean condition, ISqlSegment column, String sql) {
        return maybeDo(condition, () -> appendSqlSegments(column, IN,
            () -> String.format("(%s)", sql)));
    }

    @Override
    public Children gtSql(boolean condition, ISqlSegment column, String sql) {
        return maybeDo(condition, () -> appendSqlSegments(column, GT,
            () -> String.format("(%s)", sql)));
    }

    @Override
    public Children geSql(boolean condition, ISqlSegment column, String sql) {
        return maybeDo(condition, () -> appendSqlSegments(column, GE,
            () -> String.format("(%s)", sql)));
    }

    @Override
    public Children ltSql(boolean condition, ISqlSegment column, String sql) {
        return maybeDo(condition, () -> appendSqlSegments(column, LT,
            () -> String.format("(%s)", sql)));
    }

    @Override
    public Children leSql(boolean condition, ISqlSegment column, String sql) {
        return maybeDo(condition, () -> appendSqlSegments(column, LE,
            () -> String.format("(%s)", sql)));
    }

    @Override
    public Children notInSql(boolean condition, ISqlSegment column, String sql) {
        return maybeDo(condition, () -> appendSqlSegments(column, NOT_IN,
            () -> String.format("(%s)", sql)));
    }

    @Override
    public Children groupBy(boolean condition, ISqlSegment column) {
        return maybeDo(condition, () -> appendSqlSegments(GROUP_BY, column));
    }

    @Override
    public Children orderBy(boolean condition, boolean isAsc, ISqlSegment column) {
        return maybeDo(condition, () -> Arrays.asList(column.getSqlSegment().split(StringPool.COMMA))
            .forEach(j -> appendSqlSegments(ORDER_BY, () -> j, isAsc ? ASC : DESC)));
    }

    @Override
    public Children having(boolean condition, String sqlHaving, Object... params) {
        return maybeDo(condition, () -> appendSqlSegments(HAVING, () -> formatSqlMaybeWithParam(sqlHaving, params)));
    }

    @Override
    public Children func(boolean condition, Consumer<Children> consumer) {
        return maybeDo(condition, () -> consumer.accept(selfOrChildren()));
    }

    /**
     * 内部自用
     * <p>NOT 关键词</p>
     */
    protected Children not(boolean condition) {
        return maybeDo(condition, () -> appendSqlSegments(NOT));
    }

    /**
     * 内部自用
     * <p>拼接 AND</p>
     */
    protected Children and(boolean condition) {
        return maybeDo(condition, () -> appendSqlSegments(AND));
    }

    /**
     * 内部自用
     * <p>拼接 LIKE 以及 值</p>
     */
    protected Children likeValue(boolean condition, SqlKeyword keyword, ISqlSegment column, Object val, SqlLike sqlLike) {
        return maybeDo(condition, () -> appendSqlSegments(column, keyword,
            () -> formatParam(SqlUtils.concatLike(val, sqlLike), null)));
    }

    /**
     * 普通查询条件
     *
     * @param condition  是否执行
     * @param column     属性
     * @param sqlKeyword SQL 关键词
     * @param val        条件值
     */
    protected Children addCondition(boolean condition, ISqlSegment column, SqlKeyword sqlKeyword, Object val, Supplier<String> mapping) {
        return maybeDo(condition, () -> appendSqlSegments(column, sqlKeyword, () -> formatParam(val, mapping)));
    }

    /**
     * 多重嵌套查询条件
     *
     * @param condition 查询条件值
     */
    protected Children addNestedCondition(boolean condition, Consumer<AbstractWrapper<T, Mut, Children>> consumer) {
        return maybeDo(condition, () -> {
            final AbstractWrapper<T, Mut, Children> instance = instance();
            consumer.accept(instance);
            appendSqlSegments(APPLY, instance);
        });
    }

    /**
     * 子类返回一个自己的新对象,必须无参构造函数,用于嵌套sql
     */
    private AbstractWrapper<T, Mut, Children> instance() {
        try {
            return getClass().getDeclaredConstructor().newInstance().setContext(getContext());
        } catch (Exception e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    /**
     * 格式化 sql
     * <p>
     * 支持 "{0}" 这种,或者 "sql {0} sql" 这种
     * 也支持 "sql {0,javaType=int,jdbcType=NUMERIC,typeHandler=xxx.xxx.MyTypeHandler} sql" 这种
     *
     * @param sqlStr 可能是sql片段
     * @param params 参数
     * @return sql片段
     */
    @SuppressWarnings("SameParameterValue")
    protected final String formatSqlMaybeWithParam(String sqlStr, Object... params) {
        if (StringUtils.isBlank(sqlStr)) {
            return null;
        }
        if (ArrayUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.length; ++i) {
                String target = Constants.LEFT_BRACE + i + Constants.RIGHT_BRACE;
                if (sqlStr.contains(target)) {
                    sqlStr = sqlStr.replace(target, formatParam(params[i], null));
                } else {
                    Matcher matcher = Pattern.compile("[{]" + i + ",[a-zA-Z0-9.,=]+}").matcher(sqlStr);
                    if (!matcher.find()) {
                        throw ExceptionUtils.mpe("Please check the syntax correctness! sql not contains: \"%s\"", target);
                    }
                    String group = matcher.group();
                    String mapping = group.substring(target.length(), group.length() - 1);
                    sqlStr = sqlStr.replace(group, formatParam(params[i], () -> mapping));
                }
            }
        }
        return sqlStr;
    }

    /**
     * 处理入参
     *
     * @param mapping 例如: "javaType=int,jdbcType=NUMERIC,typeHandler=xxx.xxx.MyTypeHandler" 这种
     * @param param   参数
     * @return value
     */
    protected final String formatParam(Object param, Supplier<String> mapping) {
        return SqlScriptUtils.safeParam(getContext().putParam(param), Objects.isNull(mapping) ? null : mapping.get());
    }

    /**
     * 函数化的做事
     *
     * @param condition 做不做
     * @param runnable  做什么
     * @return Children
     */
    protected final Children maybeDo(boolean condition, Runnable runnable) {
        if (condition) {
            runnable.run();
        }
        return selfOrChildren();
    }

    /**
     * 获取in表达式 包含括号
     *
     * @param value 集合
     */
    protected ISqlSegment inExpression(Collection<?> value) {
        if (CollectionUtils.isEmpty(value)) {
            return () -> "()";
        }
        return () -> value.stream().map(i -> formatParam(i, null))
            .collect(joining(StringPool.COMMA, StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET));
    }

    /**
     * 获取in表达式 包含括号
     *
     * @param values 数组
     */
    protected ISqlSegment inExpression(Object[] values) {
        if (ArrayUtils.isEmpty(values)) {
            return () -> "()";
        }
        return () -> Arrays.stream(values).map(i -> formatParam(i, null))
            .collect(joining(StringPool.COMMA, StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET));
    }

    public WrapperNestedContext<T> getContext() {
        if (context == null) {
            context = new WrapperNestedContext<>();
        }
        return context;
    }

    @Override
    public void clear() {
        getContext().clear();
        lastSql.toEmpty();
        sqlComment.toEmpty();
        sqlFirst.toEmpty();
        expression.clear();
        selectBodyOrSetSql.clear();
    }

    /**
     * 添加 where 片段
     *
     * @param sqlSegments ISqlSegment 数组
     */
    protected void appendSqlSegments(ISqlSegment... sqlSegments) {
        expression.add(sqlSegments);
    }

    /**
     * 是否使用默认注解 {@link OrderBy} 排序
     *
     * @return true 使用 false 不使用
     */
    public boolean isUseAnnotationOrderBy() {
        final String _sqlSegment = this.getSqlSegment();
        if (StringUtils.isBlank(_sqlSegment)) {
            return true;
        }
        final String _sqlSegmentUpper = _sqlSegment.toUpperCase();
        return !(_sqlSegmentUpper.contains(Constants.ORDER_BY) || _sqlSegmentUpper.contains(Constants.LIMIT));
    }

    @Override
    public String getSqlSegment() {
        return getExpression().getSqlSegment() + lastSql.getStringValue();
    }

    @Override
    public String getSqlComment() {
        if (StringUtils.isNotBlank(sqlComment.getStringValue())) {
            return "/*" + sqlComment.getStringValue() + "*/";
        }
        return null;
    }

    @Override
    public String getSqlFirst() {
        if (StringUtils.isNotBlank(sqlFirst.getStringValue())) {
            return sqlFirst.getStringValue();
        }
        return null;
    }

    @Override
    public MergeSegments getExpression() {
        return expression;
    }

    @Override
    @SuppressWarnings("all")
    public Children clone() {
        return SerializationUtils.clone(selfOrChildren());
    }

    @Override
    public String checkStrCol(String column) {
        getContext().checkStringColumn(column);
        return column;
    }
}
