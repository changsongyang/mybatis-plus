package com.baomidou.mybatisplus.test.h2

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.core.MybatisConfiguration
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import org.apache.ibatis.builder.MapperBuilderAssistant
import org.apache.ibatis.reflection.MetaObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 *
 * @author nieqiurong
 */
class MetaObjectHandlerTest {

    @Test
    fun println() {
        val clzs = arrayOf(
            String::class, Long::class, Int::class, Double::class, Float::class,
            Short::class, Byte::class, Boolean::class, Date::class,
            Time::class,
            Timestamp::class, java.sql.Date::class, LocalDate::class, LocalDateTime::class, BigInteger::class,
            BigDecimal::class, BigInteger::class
        )
        for (clz in clzs) {
            println("kotlinType:" + clz.simpleName + "----->" + "JavaObjectType:" + clz.javaObjectType.name + "---->" + "JavaType:" + clz.java.name)
        }
    }

    @Test
    fun test() {
        val configuration = MybatisConfiguration()
        val mapperBuilderAssistant = MapperBuilderAssistant(configuration, "")
        val tableInfo = TableInfoHelper.initTableInfo(mapperBuilderAssistant, Demo::class.java)
        for (tableFieldInfo in tableInfo.fieldList) {
            println(tableFieldInfo.property + "----->" + tableFieldInfo.propertyType.name)
        }
        val demo = Demo()
        val metaObjectHandler = object : MetaObjectHandler {
            override fun insertFill(metaObject: MetaObject) {
                this.strictInsertFill(metaObject, "testString", String::class.java, "123")
                this.strictInsertFill(metaObject, "testLong", Long::class.javaObjectType, 123456L)
                this.strictInsertFill(metaObject, "testInt", Int::class.javaObjectType, 123)
                this.strictInsertFill(
                    metaObject,
                    "testLocalDateTime",
                    LocalDateTime::class.javaObjectType,
                    LocalDateTime.now()
                )
                this.strictInsertFill(metaObject, "testBoolean", Boolean::class.javaObjectType, false)
                this.strictInsertFill(metaObject, "testDate", Date::class.javaObjectType, Date())
                this.strictInsertFill(metaObject, "testLocalDate", LocalDate::class.javaObjectType, LocalDate.now())
            }

            override fun updateFill(metaObject: MetaObject) {

            }
        }
        val metaObject: MetaObject = configuration.newMetaObject(demo)
        metaObjectHandler.insertFill(metaObject)
        Assertions.assertNotNull(demo.testString)
        Assertions.assertNotNull(demo.testInt)
        Assertions.assertNotNull(demo.testLong)
        Assertions.assertNotNull(demo.testDate)
        Assertions.assertNotNull(demo.testLocalDateTime)
        Assertions.assertNotNull(demo.testBoolean)
        Assertions.assertNotNull(demo.testLocalDate)
        println(demo)
    }

}

class Demo {

    @TableField(fill = FieldFill.INSERT_UPDATE)
    var testString: String? = null

    @TableField(fill = FieldFill.INSERT_UPDATE)
    var testInt: Int? = null

    @TableField(fill = FieldFill.INSERT_UPDATE)
    var testLong: Long? = null

    @TableField(fill = FieldFill.INSERT_UPDATE)
    var testDate: Date? = null

    @TableField(fill = FieldFill.INSERT_UPDATE)
    var testLocalDateTime: LocalDateTime? = null

    @TableField(fill = FieldFill.INSERT_UPDATE)
    var testBoolean: Boolean? = null

    @TableField(fill = FieldFill.INSERT_UPDATE)
    var testLocalDate: LocalDate? = null

    override fun toString(): String {
        return "Demo(testBoolean=$testBoolean, testString=$testString, testInt=$testInt, testLong=$testLong, testDate=$testDate, testLocalDateTime=$testLocalDateTime, testLocalDate=$testLocalDate)"
    }

}
