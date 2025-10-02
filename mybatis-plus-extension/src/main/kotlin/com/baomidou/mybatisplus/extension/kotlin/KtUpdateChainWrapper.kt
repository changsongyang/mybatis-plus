///*
// * Copyright (c) 2011-2025, baomidou (jobob@qq.com).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.baomidou.mybatisplus.extension.kotlin
//
//import com.baomidou.mybatisplus.core.conditions.ISqlSegment
//import com.baomidou.mybatisplus.core.conditions.update.Update
//import com.baomidou.mybatisplus.core.mapper.BaseMapper
//import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper
//import com.baomidou.mybatisplus.extension.conditions.update.ChainUpdate
//import java.util.function.Supplier
//import kotlin.reflect.KProperty1
//
///**
// * @author FlyInWind
// * @since 2020-10-18
// */
//@Suppress("serial")
//open class KtUpdateChainWrapper<T : Any> : AbstractChainWrapper<T, KProperty1<in T, *>, KtUpdateChainWrapper<T>, KtUpdateWrapper<T>>,
//    Update<KProperty1<in T, *>, KtUpdateChainWrapper<T>>, ChainUpdate<T, KtUpdateChainWrapper<T>> {
//
//    constructor() : super()
//
//    constructor(baseMapper: BaseMapper<T>) : super(baseMapper)
//
//    constructor(entityClass: Class<T>) : super(entityClass)
//
//    override fun instanceDelegate(): KtUpdateWrapper<T> = KtUpdateWrapper()
//
//    override fun set(condition: Boolean, column: ISqlSegment, value: Any, mapping: Supplier<String>?): KtUpdateChainWrapper<T> {
//        delegateWrapper.set(condition, column, value, mapping!!)
//        return selfOrChildren()
//    }
//
//    override fun setSql(condition: Boolean, setSql: String, vararg params: Any): KtUpdateChainWrapper<T> {
//        delegateWrapper.setSql(condition, setSql, *params)
//        return selfOrChildren()
//    }
//
//    override fun setIncrBy(condition: Boolean, column: ISqlSegment, value: Number): KtUpdateChainWrapper<T> {
//        delegateWrapper.setIncrBy(condition, column, value)
//        return selfOrChildren()
//    }
//
//    override fun setDecrBy(condition: Boolean, column: ISqlSegment, value: Number): KtUpdateChainWrapper<T> {
//        delegateWrapper.setDecrBy(condition, column, value)
//        return selfOrChildren()
//    }
//}
