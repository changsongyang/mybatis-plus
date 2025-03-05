package com.baomidou.mybatisplus.test.h2

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import java.time.LocalDateTime

/**
 * @author nieqiurong
 */
class MyMetaObjectHandler : MetaObjectHandler {

    override fun insertFill(metaObject: MetaObject) {
        this.strictInsertFill(metaObject, "createdDt", LocalDateTime::class.javaObjectType, LocalDateTime.now())
    }

    override fun updateFill(metaObject: MetaObject) {
        this.strictUpdateFill(metaObject, "lastUpdatedDt", LocalDateTime::class.javaObjectType, LocalDateTime.now())
    }

}
