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
package com.baomidou.mybatisplus.core.toolkit;

import com.baomidou.mybatisplus.core.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * Wrapper 条件构造
 *
 * @author Caratacus
 */
public abstract class Wrappers {

    /**
     * 获取 QueryWrapper&lt;T&gt;
     *
     * @param <T> 实体类泛型
     * @return QueryWrapper&lt;T&gt;
     */
    public static <T> QueryWrapper<T> query() {
        return new QueryWrapper<>();
    }

    /**
     * 获取 QueryWrapper&lt;T&gt;
     *
     * @param entity 实体类
     * @param <T>    实体类泛型
     * @return QueryWrapper&lt;T&gt;
     */
    public static <T> QueryWrapper<T> query(T entity) {
        return new QueryWrapper<>(entity);
    }

    /**
     * 获取 QueryWrapper&lt;T&gt;
     *
     * @param entityClass 实体类class
     * @param <T>         实体类泛型
     * @return QueryWrapper&lt;T&gt;
     */
    public static <T> QueryWrapper<T> query(Class<T> entityClass) {
        return new QueryWrapper<>(entityClass);
    }

    /**
     * 获取 UpdateWrapper&lt;T&gt;
     *
     * @param <T> 实体类泛型
     * @return UpdateWrapper&lt;T&gt;
     */
    public static <T> UpdateWrapper<T> update() {
        return new UpdateWrapper<>();
    }

    /**
     * 获取 UpdateWrapper&lt;T&gt;
     *
     * @param entity 实体类
     * @param <T>    实体类泛型
     * @return UpdateWrapper&lt;T&gt;
     */
    public static <T> UpdateWrapper<T> update(T entity) {
        return new UpdateWrapper<>(entity);
    }

    /**
     * 获取 UpdateWrapper&lt;T&gt;
     *
     * @param entityClass 实体类class
     * @param <T>         实体类泛型
     * @return UpdateWrapper&lt;T&gt;
     */
    public static <T> UpdateWrapper<T> update(Class<T> entityClass) {
        return new UpdateWrapper<>(entityClass);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * 链式查询 普通
     *
     * @return QueryWrapper 的包装类
     */
    public static <T> QueryChainWrapper<T> queryChain(BaseMapper<T> mapper) {
        return new QueryChainWrapper<>(mapper);
    }

    public static <T> QueryChainWrapper<T> queryChain(Class<T> entityClass) {
        return new QueryChainWrapper<>(entityClass);
    }

    public static <T> QueryChainWrapper<T> queryChain(BaseMapper<T> mapper, Class<T> entityClass) {
        return new QueryChainWrapper<>(mapper).setEntityClass(entityClass);
    }

    public static <T> QueryChainWrapper<T> queryChain(BaseMapper<T> mapper, T entity) {
        return new QueryChainWrapper<>(mapper).setEntity(entity);
    }

    /**
     * 链式更改 普通
     *
     * @return UpdateWrapper 的包装类
     */
    public static <T> UpdateChainWrapper<T> updateChain(BaseMapper<T> mapper) {
        return new UpdateChainWrapper<>(mapper);
    }

    public static <T> UpdateChainWrapper<T> updateChain(Class<T> entityClass) {
        return new UpdateChainWrapper<>(entityClass);
    }

    public static <T> UpdateChainWrapper<T> updateChain(BaseMapper<T> mapper, Class<T> entityClass) {
        return new UpdateChainWrapper<>(mapper).setEntityClass(entityClass);
    }

    public static <T> UpdateChainWrapper<T> updateChain(BaseMapper<T> mapper, T entity) {
        return new UpdateChainWrapper<>(mapper).setEntity(entity);
    }
}
