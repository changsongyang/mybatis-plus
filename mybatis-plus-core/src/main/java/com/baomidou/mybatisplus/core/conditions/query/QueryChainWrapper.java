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
package com.baomidou.mybatisplus.core.conditions.query;

import com.baomidou.mybatisplus.core.conditions.AbstractChainWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.SqlHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Query Chain Wrapper
 *
 * @author miemie
 * @since 2018-12-19
 */
public class QueryChainWrapper<T> extends AbstractChainWrapper<T, QueryChainWrapper<T>, QueryWrapper<T>>
    implements Query<T, QueryChainWrapper<T>> {

    public QueryChainWrapper() {
        super();
    }

    public QueryChainWrapper(BaseMapper<T> baseMapper) {
        super(baseMapper);
    }

    public QueryChainWrapper(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected QueryWrapper<T> instanceDelegate() {
        return new QueryWrapper<>();
    }

    @Override
    public QueryChainWrapper<T> select(boolean condition, ISqlSegment sqlSegment) {
        delegateWrapper.select(condition, sqlSegment);
        return typedThis;
    }

    @Override
    public QueryChainWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        delegateWrapper.select(entityClass, predicate);
        return typedThis;
    }

    @Override
    public String getSqlSelect() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getSqlSelect");
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 获取集合
     *
     * @return 集合
     */
    public List<T> list() {
        return execute(mapper -> mapper.selectList(delegateWrapper));
    }

    /**
     * 获取集合
     *
     * @param page 分页条件
     * @return 集合记录
     * @since 3.5.3.2
     */
    public List<T> list(IPage<T> page) {
        return execute(mapper -> mapper.selectList(page, delegateWrapper));
    }

    /**
     * 获取单个
     *
     * @return 单个
     */
    public T one() {
        return execute(mapper -> mapper.selectOne(delegateWrapper));
    }

    /**
     * 获取单个
     *
     * @return 单个
     * @since 3.3.0
     */
    public Optional<T> oneOpt() {
        return Optional.ofNullable(one());
    }

    /**
     * 获取 count
     *
     * @return count
     */
    public Long count() {
        return typedThis.execute(mapper -> SqlHelper.retCount(mapper.selectCount(delegateWrapper)));
    }

    /**
     * 判断数据是否存在
     *
     * @return true 存在 false 不存在
     */
    public boolean exists() {
        return this.count() > 0;
    }

    /**
     * 获取分页数据
     *
     * @param page 分页条件
     * @return 分页数据
     */
    public <E extends IPage<T>> E page(E page) {
        return typedThis.execute(mapper -> mapper.selectPage(page, delegateWrapper));
    }
}
