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
package com.baomidou.mybatisplus.extension.kotlin

import com.baomidou.mybatisplus.core.conditions.ISqlSegment
import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper
import com.baomidou.mybatisplus.extension.conditions.query.ChainQuery
import java.util.function.Predicate
import kotlin.reflect.KProperty1

/**
 * @author FlyInWind
 * @since 2020-10-18
 */
@Suppress("serial")
open class KtQueryChainWrapper<T : Any> : AbstractChainWrapper<T, KProperty1<in T, *>, KtQueryChainWrapper<T>, KtQueryWrapper<T>>,
    Query<T, KProperty1<in T, *>, KtQueryChainWrapper<T>>, ChainQuery<T, KtQueryChainWrapper<T>> {

    constructor() : super()

    constructor(baseMapper: BaseMapper<T>) : super(baseMapper)

    constructor(entityClass: Class<T>) : super(entityClass)

    override fun instanceDelegate(): KtQueryWrapper<T> = KtQueryWrapper()

    override fun select(condition: Boolean, sqlSegment: ISqlSegment): KtQueryChainWrapper<T> {
        delegateWrapper.select(condition, sqlSegment)
        return selfOrChildren()
    }

    override fun select(entityClass: Class<T>, predicate: Predicate<TableFieldInfo>): KtQueryChainWrapper<T> {
        delegateWrapper.select(entityClass, predicate)
        return selfOrChildren()
    }
}
