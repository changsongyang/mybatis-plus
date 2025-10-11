package com.baomidou.mybatisplus.test.kotlin

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.mapper.BaseMapper

interface UserMapper : BaseMapper<User> {

    fun hello(): String {
        return "hello baomidou!";
    }

    fun findById(id: Int): User? {
        return selectOne(QueryWrapper(User::class.java).eq(User::id, id));
    }

}
