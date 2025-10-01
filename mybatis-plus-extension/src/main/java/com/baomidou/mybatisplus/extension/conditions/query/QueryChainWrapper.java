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
package com.baomidou.mybatisplus.extension.conditions.query;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper;

import java.util.function.Predicate;

/**
 * Query Chain Wrapper
 *
 * @author miemie
 * @since 2018-12-19
 */
@SuppressWarnings({"serial"})
public class QueryChainWrapper<T> extends AbstractChainWrapper<T, SFunction<T, ?>, QueryChainWrapper<T>, QueryWrapper<T>>
    implements Query<T, SFunction<T, ?>, QueryChainWrapper<T>>, ChainQuery<T, QueryChainWrapper<T>> {

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
        return selfOrChildren();
    }

    @Override
    public QueryChainWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        delegateWrapper.select(entityClass, predicate);
        return selfOrChildren();
    }
}
