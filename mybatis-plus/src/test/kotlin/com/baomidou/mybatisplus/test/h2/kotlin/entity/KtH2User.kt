package com.baomidou.mybatisplus.test.h2.kotlin.entity

import com.baomidou.mybatisplus.annotation.*
import com.baomidou.mybatisplus.test.h2.enums.AgeEnum
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@TableName("h2user")
class KtH2User {

    @TableId
    var testId: Long? = null

    var name: String? = null

    var age: AgeEnum? = null

    var price: BigDecimal? = null

    var testType: Int? = null

    @TableField("`desc`")
    var desc: String? = null

    @TableField(select = false)
    var testDate: Date? = null

    @Version
    var version: Int? = null

    @TableLogic
    val deleted: Int? = null

    @TableField(fill = FieldFill.INSERT)
    var createdDt: LocalDateTime? = null

    @TableField(fill = FieldFill.UPDATE)
    var lastUpdatedDt: LocalDateTime? = null

}
