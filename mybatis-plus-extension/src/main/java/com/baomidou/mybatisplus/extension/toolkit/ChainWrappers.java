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
package com.baomidou.mybatisplus.extension.toolkit;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;

/**
 * 快捷构造 chain 式调用的工具类
 *
 * @author miemie
 * @since 2019-11-28
 * @since 3.3.0
 */
public final class ChainWrappers {

    private ChainWrappers() {
        // ignore
    }

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

    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return QueryWrapper 的包装类
     */
    public static <T> QueryChainWrapper<T> lambdaQueryChain(BaseMapper<T> mapper) {
        return new QueryChainWrapper<>(mapper);
    }

    public static <T> QueryChainWrapper<T> lambdaQueryChain(Class<T> entityClass) {
        return new QueryChainWrapper<>(entityClass);
    }

    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return QueryWrapper 的包装类
     */
    public static <T> QueryChainWrapper<T> lambdaQueryChain(BaseMapper<T> mapper, T entity) {
        return new QueryChainWrapper<>(mapper).setEntity(entity);
    }

    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return QueryWrapper 的包装类
     */
    public static <T> QueryChainWrapper<T> lambdaQueryChain(BaseMapper<T> mapper, Class<T> entityClass) {
        return new QueryChainWrapper<>(mapper).setEntityClass(entityClass);
    }

    /**
     * 链式查询 lambda 式
     * 仅支持 Kotlin
     *
     * @return KtQueryWrapper 的包装类
     */
    public static <T> QueryChainWrapper<T> ktQueryChain(BaseMapper<T> mapper, Class<T> entityClass) {
        return new QueryChainWrapper<>(mapper).setEntityClass(entityClass);
    }

    /**
     * 链式查询 lambda 式
     * 仅支持 Kotlin
     *
     * @return KtQueryWrapper 的包装类
     */
    public static <T> QueryChainWrapper<T> ktQueryChain(BaseMapper<T> mapper, T entity) {
        return new QueryChainWrapper<>(mapper).setEntity(entity);
    }

    /**
     * 链式查询 lambda 式
     * 仅支持 Kotlin
     * 仅传 entityClass 实体类
     *
     * @return KtQueryWrapper 的包装类
     */
    public static <T> QueryChainWrapper<T> ktQueryChain(Class<T> entityClass) {
        return new QueryChainWrapper<>(entityClass);
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

    /**
     * 链式更改 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return UpdateWrapper 的包装类
     */
    public static <T> UpdateChainWrapper<T> lambdaUpdateChain(BaseMapper<T> mapper) {
        return new UpdateChainWrapper<>(mapper);
    }

    public static <T> UpdateChainWrapper<T> lambdaUpdateChain(Class<T> entityClass) {
        return new UpdateChainWrapper<>(entityClass);
    }

    /**
     * 链式更改 lambda 式
     * 仅支持 Kotlin
     *
     * @return KtQueryWrapper 的包装类
     */
    public static <T> UpdateChainWrapper<T> ktUpdateChain(BaseMapper<T> mapper, Class<T> entityClass) {
        return new UpdateChainWrapper<>(mapper).setEntityClass(entityClass);
    }


    /**
     * 链式更改 lambda 式
     * 仅支持 Kotlin
     *
     * @return KtQueryWrapper 的包装类
     */
    public static <T> UpdateChainWrapper<T> ktUpdateChain(BaseMapper<T> mapper, T entity) {
        return new UpdateChainWrapper<>(mapper).setEntity(entity);
    }

    /**
     * 链式更改 lambda 式
     * 仅支持 Kotlin
     * 仅传 entityClass 实体类
     *
     * @return KtUpdateWrapper 的包装类
     */
    public static <T> UpdateChainWrapper<T> ktUpdateChain(Class<T> entityClass) {
        return new UpdateChainWrapper<>(entityClass);
    }
}
