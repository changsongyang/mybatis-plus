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
package com.baomidou.mybatisplus.extension.conditions.update;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.util.function.Supplier;

/**
 * Update Chain Wrapper
 *
 * @author miemie
 * @since 2018-12-19
 */
@SuppressWarnings({"serial"})
public class UpdateChainWrapper<T> extends AbstractChainWrapper<T, UpdateChainWrapper<T>, UpdateWrapper<T>>
    implements Update<T, UpdateChainWrapper<T>> {

    public UpdateChainWrapper() {
        super();
    }

    public UpdateChainWrapper(BaseMapper<T> baseMapper) {
        super(baseMapper);
    }

    public UpdateChainWrapper(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected UpdateWrapper<T> instanceDelegate() {
        return new UpdateWrapper<>();
    }

    @Override
    public UpdateChainWrapper<T> set(boolean condition, ISqlSegment column, Object value, Supplier<String> mapping) {
        delegateWrapper.set(condition, column, value, mapping);
        return typedThis;
    }

    @Override
    public UpdateChainWrapper<T> setSql(boolean condition, String setSql, Object... params) {
        delegateWrapper.setSql(condition, setSql, params);
        return typedThis;
    }

    @Override
    public UpdateChainWrapper<T> setIncrBy(boolean condition, ISqlSegment column, Number value) {
        delegateWrapper.setIncrBy(condition, column, value);
        return typedThis;
    }

    @Override
    public UpdateChainWrapper<T> setDecrBy(boolean condition, ISqlSegment column, Number value) {
        delegateWrapper.setDecrBy(condition, column, value);
        return typedThis;
    }

    @Override
    public String getSqlSet() {
        throw ExceptionUtils.mpe("can not use this method for \"%s\"", "getSqlSet");
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 更新数据
     * <p>此方法无法进行自动填充,如需自动填充请使用{@link #update(Object)}</p>
     *
     * @return 是否成功
     */
    public boolean update() {
        return update(null);
    }

    /**
     * 更新数据
     *
     * @param entity 实体类(当entity为空时无法进行自动填充)
     * @return 是否成功
     */
    public boolean update(T entity) {
        return execute(mapper -> SqlHelper.retBool(mapper.update(entity, delegateWrapper)));
    }

    /**
     * 删除数据
     *
     * @return 是否成功
     */
    public boolean remove() {
        return execute(mapper -> SqlHelper.retBool(mapper.delete(delegateWrapper)));
    }
}
