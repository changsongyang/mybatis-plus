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

import com.baomidou.mybatisplus.core.conditions.*;
import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.conditions.interfaces.Nested;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
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
public abstract class AbstractChainWrapper<T, Mut, Children extends AbstractChainWrapper<T, Mut, Children, Param>,
    Param extends AbstractWrapper<T, Mut, Param>> extends Wrapper<T> implements Compare<Mut, Children>, Join<Children>,
    Nested<AbstractWrapper<T, Mut, Param>, Children>, SelfChildren<Children> {
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

    @Override
    public Children selfOrChildren() {
        return (Children) this;
    }

    public Param delegate() {
        return delegateWrapper;
    }

    protected abstract Param instanceDelegate();

    public Children setBaseMapper(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
        return selfOrChildren();
    }

    public Children setContext(WrapperNestedContext<T> context) {
        delegateWrapper.setContext(context);
        return selfOrChildren();
    }

    public Children setEntity(T entity) {
        delegateWrapper.setEntity(entity);
        return selfOrChildren();
    }

    public Children setEntityClass(Class<T> entityClass) {
        delegateWrapper.setEntityClass(entityClass);
        return selfOrChildren();
    }

    @Override
    public <V> Children allEq(boolean condition, Map<String, V> params, boolean null2IsNull) {
        delegateWrapper.allEq(condition, params, null2IsNull);
        return selfOrChildren();
    }

    @Override
    public <V> Children allEq(boolean condition, BiPredicate<String, V> filter, Map<String, V> params, boolean null2IsNull) {
        delegateWrapper.allEq(condition, filter, params, null2IsNull);
        return selfOrChildren();
    }

    @Override
    public Children eq(boolean condition, ISqlSegment column, Object val, Supplier<String> mapping) {
        delegateWrapper.eq(condition, column, val, mapping);
        return selfOrChildren();
    }

    @Override
    public Children ne(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.ne(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children gt(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.gt(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children ge(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.ge(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children lt(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.lt(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children le(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.le(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children between(boolean condition, ISqlSegment column, Object val1, Object val2) {
        delegateWrapper.between(condition, column, val1, val2);
        return selfOrChildren();
    }

    @Override
    public Children notBetween(boolean condition, ISqlSegment column, Object val1, Object val2) {
        delegateWrapper.notBetween(condition, column, val1, val2);
        return selfOrChildren();
    }

    @Override
    public Children like(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.like(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children notLike(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.notLike(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children notLikeLeft(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.notLikeLeft(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children notLikeRight(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.notLikeRight(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children likeLeft(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.likeLeft(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children likeRight(boolean condition, ISqlSegment column, Object val) {
        delegateWrapper.likeRight(condition, column, val);
        return selfOrChildren();
    }

    @Override
    public Children isNull(boolean condition, ISqlSegment column) {
        delegateWrapper.isNull(condition, column);
        return selfOrChildren();
    }

    @Override
    public Children isNotNull(boolean condition, ISqlSegment column) {
        delegateWrapper.isNotNull(condition, column);
        return selfOrChildren();
    }

    @Override
    public Children in(boolean condition, ISqlSegment column, Collection<?> coll) {
        delegateWrapper.in(condition, column, coll);
        return selfOrChildren();
    }

    @Override
    public Children in(boolean condition, ISqlSegment column, Object... values) {
        delegateWrapper.in(condition, column, values);
        return selfOrChildren();
    }

    @Override
    public Children notIn(boolean condition, ISqlSegment column, Collection<?> coll) {
        delegateWrapper.notIn(condition, column, coll);
        return selfOrChildren();
    }

    @Override
    public Children notIn(boolean condition, ISqlSegment column, Object... values) {
        delegateWrapper.notIn(condition, column, values);
        return selfOrChildren();
    }

    @Override
    public Children eqSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.eqSql(condition, column, sql);
        return selfOrChildren();
    }

    @Override
    public Children inSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.inSql(condition, column, sql);
        return selfOrChildren();
    }

    @Override
    public Children gtSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.gtSql(condition, column, sql);
        return selfOrChildren();
    }

    @Override
    public Children geSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.geSql(condition, column, sql);
        return selfOrChildren();
    }

    @Override
    public Children ltSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.ltSql(condition, column, sql);
        return selfOrChildren();
    }

    @Override
    public Children leSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.leSql(condition, column, sql);
        return selfOrChildren();
    }

    @Override
    public Children notInSql(boolean condition, ISqlSegment column, String sql) {
        delegateWrapper.notInSql(condition, column, sql);
        return selfOrChildren();
    }

    @Override
    public Children groupBy(boolean condition, ISqlSegment column) {
        delegateWrapper.groupBy(condition, column);
        return selfOrChildren();
    }

    @Override
    public Children orderBy(boolean condition, boolean isAsc, ISqlSegment column) {
        delegateWrapper.orderBy(condition, isAsc, column);
        return selfOrChildren();
    }

    @Override
    public Children having(boolean condition, String sqlHaving, Object... params) {
        delegateWrapper.having(condition, sqlHaving, params);
        return selfOrChildren();
    }

    @Override
    public Children func(boolean condition, Consumer<Children> consumer) {
        if (condition) {
            consumer.accept(selfOrChildren());
        }
        return selfOrChildren();
    }

    @Override
    public Children or(boolean condition) {
        delegateWrapper.or(condition);
        return selfOrChildren();
    }

    @Override
    public Children apply(boolean condition, String applySql, Object... values) {
        delegateWrapper.apply(condition, applySql, values);
        return selfOrChildren();
    }

    @Override
    public Children last(boolean condition, String lastSql) {
        delegateWrapper.last(condition, lastSql);
        return selfOrChildren();
    }

    @Override
    public Children comment(boolean condition, String comment) {
        delegateWrapper.comment(condition, comment);
        return selfOrChildren();
    }

    @Override
    public Children first(boolean condition, String firstSql) {
        delegateWrapper.first(condition, firstSql);
        return selfOrChildren();
    }

    @Override
    public Children exists(boolean condition, String existsSql, Object... values) {
        delegateWrapper.exists(condition, existsSql, values);
        return selfOrChildren();
    }

    @Override
    public Children notExists(boolean condition, String existsSql, Object... values) {
        delegateWrapper.notExists(condition, existsSql, values);
        return selfOrChildren();
    }

    @Override
    public Children and(boolean condition, Consumer<AbstractWrapper<T, Mut, Param>> consumer) {
        delegateWrapper.and(condition, consumer);
        return selfOrChildren();
    }

    @Override
    public Children or(boolean condition, Consumer<AbstractWrapper<T, Mut, Param>> consumer) {
        delegateWrapper.or(condition, consumer);
        return selfOrChildren();
    }

    @Override
    public Children nested(boolean condition, Consumer<AbstractWrapper<T, Mut, Param>> consumer) {
        delegateWrapper.nested(condition, consumer);
        return selfOrChildren();
    }

    @Override
    public Children not(boolean condition, Consumer<AbstractWrapper<T, Mut, Param>> consumer) {
        delegateWrapper.not(condition, consumer);
        return selfOrChildren();
    }

    @Override
    public String getSqlSegment() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getSqlSegment");
    }

    @Override
    public String getSqlFirst() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getSqlFirst");
    }

    @Override
    public String getSqlSelect() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getSqlSelect");
    }

    @Override
    public String getSqlSet() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getSqlSet");
    }

    @Override
    public String getSqlComment() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getSqlComment");
    }

    @Override
    public String getTargetSql() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getTargetSql");
    }

    @Override
    public T getEntity() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getEntity");
    }

    @Override
    public MergeSegments getExpression() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getExpression");
    }

    @Override
    public String getCustomSqlSegment() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getCustomSqlSegment");
    }

    @Override
    public void clear() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "clear");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "clone");
    }

    @Override
    public String convMut2ColInSel(Mut mutable) {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "convMut2ColInSel");
    }

    @Override
    public String convMut2Col(Mut mutable) {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "convMut2Col");
    }

    @Override
    public String convMut2ColMapping(Mut mutable) {
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
