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
package com.baomidou.mybatisplus.extension.conditions;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.WrapperNestedContext;
import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.conditions.interfaces.Nested;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 所有包装类都继承此抽象类,此抽象类代理了大部分生成 where 条件的方法
 * <li> 泛型: Children ,表示子类 </li>
 * <li> 泛型: Param ,表示子类所包装的具体 Wrapper 类型 </li>
 *
 * @author miemie
 * @since 2018-12-19
 */
@SuppressWarnings({"unchecked", "serial"})
public abstract class AbstractChainWrapper<T, Children extends AbstractChainWrapper<T, Children, Param>,
    Param extends AbstractWrapper<T, Param>> implements Compare<T, Children>, Join<Children>,
    Nested<AbstractWrapper<T, Param>, Children> {
    protected final Children typedThis = (Children) this;
    /**
     * 子类所包装的具体 Wrapper 类型 delegate
     */
    protected final Param delegateWrapper = instanceDelegate();
    protected BaseMapper<T> baseMapper = null;

    /**
     * 必须的构造函数
     */
    public AbstractChainWrapper() {
    }

    public AbstractChainWrapper(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    public AbstractChainWrapper(Class<T> entityClass) {
        setEntityClass(entityClass);
    }

    protected abstract Param instanceDelegate();

    public Children setBaseMapper(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
        return typedThis;
    }

    public Children setContext(WrapperNestedContext<T> context) {
        delegateWrapper.setContext(context);
        return typedThis;
    }

    public Children setEntity(T entity) {
        delegateWrapper.setEntity(entity);
        return typedThis;
    }

    public Children setEntityClass(Class<T> entityClass) {
        delegateWrapper.setEntityClass(entityClass);
        return typedThis;
    }

    @Override
    public <V> Children allEq(boolean condition, Map<String, V> params, boolean null2IsNull) {
        delegateWrapper.allEq(condition, params, null2IsNull);
        return typedThis;
    }

    @Override
    public <V> Children allEq(boolean condition, BiPredicate<String, V> filter, Map<String, V> params, boolean null2IsNull) {
        delegateWrapper.allEq(condition, filter, params, null2IsNull);
        return typedThis;
    }

    @Override
    public Children eq(boolean condition, ISqlSegment column, Object val, Supplier<String> mapping) {
        delegateWrapper.eq(condition, column, val, mapping);
        return typedThis;
    }

    @Override
    public Children ne(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.ne(condition, column, val);
        return typedThis;
    }

    @Override
    public Children gt(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.gt(condition, column, val);
        return typedThis;
    }

    @Override
    public Children ge(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.ge(condition, column, val);
        return typedThis;
    }

    @Override
    public Children lt(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.lt(condition, column, val);
        return typedThis;
    }

    @Override
    public Children le(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.le(condition, column, val);
        return typedThis;
    }

    @Override
    public Children between(boolean condition, ISqlSegment column, Object val1, Object val2) {
        delegateWrapper.between(condition, column, val1, val2);
        return typedThis;
    }

    @Override
    public Children notBetween(boolean condition, ISqlSegment column, Object val1, Object val2) {
        delegateWrapper.notBetween(condition, column, val1, val2);
        return typedThis;
    }

    @Override
    public Children like(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.like(condition, column, val);
        return typedThis;
    }

    @Override
    public Children notLike(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.notLike(condition, column, val);
        return typedThis;
    }

    @Override
    public Children notLikeLeft(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.notLikeLeft(condition, column, val);
        return typedThis;
    }

    @Override
    public Children notLikeRight(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.notLikeRight(condition, column, val);
        return typedThis;
    }

    @Override
    public Children likeLeft(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.likeLeft(condition, column, val);
        return typedThis;
    }

    @Override
    public Children likeRight(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.likeRight(condition, column, val);
        return typedThis;
    }

    @Override
    public Children isNull(boolean condition, ISqlSegment column) {
        delegateWrapper.isNull(condition, column);
        return typedThis;
    }

    @Override
    public Children isNotNull(boolean condition, ISqlSegment column) {
        delegateWrapper.isNotNull(condition, column);
        return typedThis;
    }

    @Override
    public Children in(boolean condition, ISqlSegment column, Collection<?> coll) {
        delegateWrapper.in(condition, column, coll);
        return typedThis;
    }

    @Override
    public Children in(boolean condition, ISqlSegment column, Object... values) {
        delegateWrapper.in(condition, column, values);
        return typedThis;
    }

    @Override
    public Children notIn(boolean condition, ISqlSegment column, Collection<?> coll) {
        delegateWrapper.notIn(condition, column, coll);
        return typedThis;
    }

    @Override
    public Children notIn(boolean condition, ISqlSegment column, Object... values) {
        delegateWrapper.notIn(condition, column, values);
        return typedThis;
    }

    @Override
    public Children eqSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.eqSql(condition, column, sql);
        return typedThis;
    }

    @Override
    public Children inSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.inSql(condition, column, sql);
        return typedThis;
    }

    @Override
    public Children gtSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.gtSql(condition, column, sql);
        return typedThis;
    }

    @Override
    public Children geSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.geSql(condition, column, sql);
        return typedThis;
    }

    @Override
    public Children ltSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.ltSql(condition, column, sql);
        return typedThis;
    }

    @Override
    public Children leSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.leSql(condition, column, sql);
        return typedThis;
    }

    @Override
    public Children notInSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.notInSql(condition, column, sql);
        return typedThis;
    }

    @Override
    public Children groupBy(boolean condition, ISqlSegment column) {
        delegateWrapper.groupBy(condition, column);
        return typedThis;
    }

    @Override
    @SafeVarargs
    public final Children groupBy(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return Compare.super.groupBy(column, columns);
    }

    @Override
    @SafeVarargs
    public final Children groupBy(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return Compare.super.groupBy(condition, column, columns);
    }

    @Override
    public Children orderBy(boolean condition, boolean isAsc, ISqlSegment column) {
        delegateWrapper.orderBy(condition, isAsc, column);
        return typedThis;
    }

    @Override
    @SafeVarargs
    public final Children orderByAsc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return Compare.super.orderByAsc(column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByAsc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return Compare.super.orderByAsc(condition, column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByDesc(SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return Compare.super.orderByDesc(column, columns);
    }

    @Override
    @SafeVarargs
    public final Children orderByDesc(boolean condition, SFunction<T, ?> column, SFunction<T, ?>... columns) {
        return Compare.super.orderByDesc(condition, column, columns);
    }

    @Override
    public Children having(boolean condition, String sqlHaving, Object... params) {
        delegateWrapper.having(condition, sqlHaving, params);
        return typedThis;
    }

    @Override
    public Children func(boolean condition, Consumer<Children> consumer) {
        if (condition) {
            consumer.accept(typedThis);
        }
        return typedThis;
    }

    @Override
    public Children or(boolean condition) {
        delegateWrapper.or(condition);
        return typedThis;
    }

    @Override
    public Children apply(boolean condition, String applySql, Object... values) {
        delegateWrapper.apply(condition, applySql, values);
        return typedThis;
    }

    @Override
    public Children last(boolean condition, String lastSql) {
        delegateWrapper.last(condition, lastSql);
        return typedThis;
    }

    @Override
    public Children comment(boolean condition, String comment) {
        delegateWrapper.comment(condition, comment);
        return typedThis;
    }

    @Override
    public Children first(boolean condition, String firstSql) {
        delegateWrapper.first(condition, firstSql);
        return typedThis;
    }

    @Override
    public Children exists(boolean condition, String existsSql, Object... values) {
        delegateWrapper.exists(condition, existsSql, values);
        return typedThis;
    }

    @Override
    public Children notExists(boolean condition, String existsSql, Object... values) {
        delegateWrapper.notExists(condition, existsSql, values);
        return typedThis;
    }

    @Override
    public Children and(boolean condition, Consumer<AbstractWrapper<T, Param>> consumer) {
        delegateWrapper.and(condition, consumer);
        return typedThis;
    }

    @Override
    public Children or(boolean condition, Consumer<AbstractWrapper<T, Param>> consumer) {
        delegateWrapper.or(condition, consumer);
        return typedThis;
    }

    @Override
    public Children nested(boolean condition, Consumer<AbstractWrapper<T, Param>> consumer) {
        delegateWrapper.nested(condition, consumer);
        return typedThis;
    }

    @Override
    public Children not(boolean condition, Consumer<AbstractWrapper<T, Param>> consumer) {
        delegateWrapper.not(condition, consumer);
        return typedThis;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "clone");
    }

    @Override
    public String convMut2ColInSel(SFunction<T, ?> mutable) {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "convMut2ColInSel");
    }

    @Override
    public String convMut2Col(SFunction<T, ?> mutable) {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "convMut2Col");
    }

    @Override
    public String convMut2ColMapping(SFunction<T, ?> mutable) {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "convMut2ColMapping");
    }

    @Override
    public String checkStrCol(String column) {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "checkStrColumn");
    }

    /**
     * 执行baseMapper操作，有baseMapper操作时使用baseMapper，没有时通过entityClass获取baseMapper，再使用
     *
     * @param function 操作
     * @param <R>      返回值
     * @return 结果
     */
    public <R> R execute(Function<BaseMapper<T>, R> function) {
        if (baseMapper != null) {
            return function.apply(baseMapper);
        }
        return SqlHelper.execute(delegateWrapper.getContext().getEntityClass(), function);
    }
}
