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
import com.baomidou.mybatisplus.core.conditions.update.Update
import com.baomidou.mybatisplus.core.toolkit.Constants
import com.baomidou.mybatisplus.core.toolkit.StringPool
import java.math.BigDecimal
import java.util.function.Supplier
import java.util.stream.Collectors.joining
import kotlin.reflect.KProperty1

/**
 * Kotlin Lambda 更新封装
 *
 * @author yangyuhan
 * @since 2018-11-02
 */
@Suppress("serial")
open class KtUpdateWrapper<T : Any> : AbstractKtWrapper<T, KtUpdateWrapper<T>>, Update<KProperty1<in T, *>, KtUpdateWrapper<T>> {

    constructor() : super()

    constructor(entity: T) {
        setEntity(entity)
    }

    constructor(entityClass: Class<T>) {
        setEntityClass(entityClass)
    }

    override fun getSqlSet(): String? {
        return if (selectBodyOrSetSql.isNullOrEmpty()) null
        else selectBodyOrSetSql.stream().collect(joining(StringPool.COMMA))
    }

    override fun set(condition: Boolean, column: ISqlSegment, value: Any, mapping: Supplier<String>): KtUpdateWrapper<T> {
        return maybeDo(condition) {
            val sql = formatParam(value, mapping)
            selectBodyOrSetSql.add(column.sqlSegment + Constants.EQUALS + sql)
        }
    }

    override fun setSql(condition: Boolean, setSql: String, vararg params: Any): KtUpdateWrapper<T> {
        return maybeDo(condition) {
            selectBodyOrSetSql.add(formatSqlMaybeWithParam(setSql, *params))
        }
    }

    override fun setIncrBy(condition: Boolean, column: ISqlSegment, value: Number): KtUpdateWrapper<T> {
        return maybeDo(condition) {
            val tc = column.sqlSegment
            selectBodyOrSetSql.add(String.format("%s=%s + %s", tc, tc, if (value is BigDecimal) value.toPlainString() else value))
        }
    }

    override fun setDecrBy(condition: Boolean, column: ISqlSegment, value: Number): KtUpdateWrapper<T> {
        return maybeDo(condition) {
            val tc = column.sqlSegment
            selectBodyOrSetSql.add(String.format("%s=%s - %s", tc, tc, if (value is BigDecimal) value.toPlainString() else value))
        }
    }
}
