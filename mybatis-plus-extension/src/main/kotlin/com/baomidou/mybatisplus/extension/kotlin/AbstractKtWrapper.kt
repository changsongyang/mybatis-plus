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

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils
import kotlin.reflect.KProperty1

/**
 * Lambda 语法使用 Wrapper
 *
 * 统一处理解析 lambda 获取 column
 *
 * @author yangyuhan,MieMie,HanChunLin
 * @since 2018-11-07
 */
@Suppress("serial")
abstract class AbstractKtWrapper<T, Children : AbstractKtWrapper<T, Children>> :
    AbstractWrapper<T, KProperty1<in T, *>, Children>() {

    override fun convMut2ColInSel(mutable: KProperty1<in T, *>): String {
        return context.getColumnCache(LambdaUtils.formatKey(mutable.name)).columnSelect
    }

    override fun convMut2Col(mutable: KProperty1<in T, *>): String {
        return context.getColumnCache(LambdaUtils.formatKey(mutable.name)).column
    }

    override fun convMut2ColMapping(mutable: KProperty1<in T, *>): String {
        return context.getColumnCache(LambdaUtils.formatKey(mutable.name)).mapping
    }
}
