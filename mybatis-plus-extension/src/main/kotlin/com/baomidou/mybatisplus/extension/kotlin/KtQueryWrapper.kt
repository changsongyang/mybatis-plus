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
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.Constants
import java.util.function.Predicate
import kotlin.reflect.KProperty1

/**
 * Kotlin Lambda 语法使用 Wrapper
 *
 * @author yangyuhan
 * @since 2018-11-02
 */
@Suppress("serial")
open class KtQueryWrapper<T : Any> : AbstractKtWrapper<T, KtQueryWrapper<T>>,
    Query<T, KProperty1<in T, *>, KtQueryWrapper<T>> {

    constructor() : super()

    constructor(entity: T) {
        setEntity(entity)
    }


    constructor(entityClass: Class<T>) {
        setEntityClass(entityClass)
    }

    override fun select(condition: Boolean, sqlSegment: ISqlSegment): KtQueryWrapper<T> {
        return maybeDo(condition) {
            selectBodyOrSetSql.add(sqlSegment.sqlSegment)
        }
    }

    override fun select(entityClass: Class<T>, predicate: Predicate<TableFieldInfo>): KtQueryWrapper<T> {
        setEntityClass(entityClass)
        selectBodyOrSetSql.add(TableInfoHelper.getTableInfo(context.getEntityClass()).chooseSelect(predicate))
        return selfOrChildren()
    }

    override fun getSqlSelect(): String? {
        return if (selectBodyOrSetSql.isNullOrEmpty()) null
        else selectBodyOrSetSql.joinToString(separator = Constants.COMMA)
    }
}
