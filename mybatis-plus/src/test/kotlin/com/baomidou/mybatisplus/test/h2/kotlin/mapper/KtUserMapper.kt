package com.baomidou.mybatisplus.test.h2.kotlin.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.test.h2.kotlin.entity.KtH2User
import org.apache.ibatis.annotations.Mapper

@Mapper
interface KtUserMapper : BaseMapper<KtH2User> {
}
